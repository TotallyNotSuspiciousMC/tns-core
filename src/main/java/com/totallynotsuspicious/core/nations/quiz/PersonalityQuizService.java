package com.totallynotsuspicious.core.nations.quiz;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import com.totallynotsuspicious.core.TNSCore;
import com.totallynotsuspicious.core.nations.CustomDialogReceivedCallback;
import com.totallynotsuspicious.core.nations.CustomEventHandlerRegistry;
import com.totallynotsuspicious.core.nations.Nation;
import com.totallynotsuspicious.core.nations.NationsManager;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.dialog.AfterAction;
import net.minecraft.dialog.DialogActionButtonData;
import net.minecraft.dialog.DialogButtonData;
import net.minecraft.dialog.DialogCommonData;
import net.minecraft.dialog.action.SimpleDialogAction;
import net.minecraft.dialog.body.DialogBody;
import net.minecraft.dialog.body.PlainMessageDialogBody;
import net.minecraft.dialog.type.Dialog;
import net.minecraft.dialog.type.NoticeDialog;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.*;

public class PersonalityQuizService {
    private static final RegistryKey<Dialog> INVALID_FORM = RegistryKey.of(RegistryKeys.DIALOG, TNSCore.id("personality_quiz/invalid_form"));
    private static final RegistryKey<Dialog> CONFIRM = RegistryKey.of(RegistryKeys.DIALOG, TNSCore.id("personality_quiz/confirm_start"));

    private static final Map<UUID, QuizForm> FORMS_IN_PROGRESS = new HashMap<>();

    public static void initialize() {
        CustomEventHandlerRegistry.register(TNSCore.id("start_personality_quiz"), (handler, payload) -> {
            FORMS_IN_PROGRESS.put(handler.getPlayer().getUuid(), new QuizForm());
            QuizForm.QuizQuestion.PLAY_STYLE.openDialog(handler.getPlayer());
            TNSCore.LOGGER.info("Player {} started a personality quiz", handler.getPlayer().getGameProfile().getName());
        });

        CustomDialogReceivedCallback.EVENT.register(PersonalityQuizService::handleQuestionSubmission);

        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            if (FORMS_IN_PROGRESS.remove(handler.getPlayer().getUuid()) != null) {
                TNSCore.LOGGER.info("Player {} left the game, so their personality quiz was cancelled", handler.getPlayer().getGameProfile().getName());
            }
        });
    }

    private static void handleQuestionSubmission(ServerPlayNetworkHandler handler, Identifier id, Optional<NbtElement> payload) {
        if (!id.getNamespace().equals("tns-core-personality-quiz")) {
            return;
        }

        QuizForm.QuizQuestion question = QuizForm.QuizQuestion.CODEC.byId(id.getPath());
        if (question == null) {
            TNSCore.LOGGER.warn(
                    "Player {} submitted an unknown quiz question.",
                    handler.getPlayer().getGameProfile().getName()
            );
            sendError(handler.getPlayer());
            return;
        }

        QuizForm form = FORMS_IN_PROGRESS.get(handler.getPlayer().getUuid());

        if (form == null) {
            TNSCore.LOGGER.warn(
                    "Invalid personality quiz answer received from {}, attempting to answer question without active quiz.",
                    handler.getPlayer().getGameProfile().getName()
            );
            sendError(handler.getPlayer());
            return;
        }

        if (payload.isEmpty()) {
            TNSCore.LOGGER.warn(
                    "Invalid personality quiz answer received from {}, No payload.",
                    handler.getPlayer().getGameProfile().getName()
            );
            sendError(handler.getPlayer());
            return;
        }

        DataResult<Pair<QuizForm.AnswerPayload, NbtElement>> result = QuizForm.AnswerPayload.CODEC
                .decode(NbtOps.INSTANCE, payload.orElseThrow());

        if (result.isError()) {
            TNSCore.LOGGER.warn(
                    "Invalid personality quiz answer received from {}, error parsing payload: {}",
                    handler.getPlayer().getGameProfile().getName(),
                    result
            );
            sendError(handler.getPlayer());
            return;
        }

        QuizForm.QuizAnswer answer = result.getOrThrow().getFirst().answer();
        form.addAnswer(question, answer);

        QuizForm.QuizQuestion nextQuestion = question.getNextQuestion();
        if (nextQuestion != null) {
            nextQuestion.openDialog(handler.getPlayer());
        } else {
            completeQuiz(handler, form);
        }
    }

    private static void sendError(ServerPlayerEntity player) {
        NationsManager.openDialog(player, INVALID_FORM);
        FORMS_IN_PROGRESS.remove(player.getUuid());
    }

    private static void completeQuiz(ServerPlayNetworkHandler handler, QuizForm completedForm) {
        Map<Nation, Integer> counts = new EnumMap<>(Nation.class);

        for (QuizForm.QuizAnswer answer : completedForm.answers()) {
            Nation nation = answer.nation();

            int count = counts.getOrDefault(nation, 0);
            counts.put(nation, count + 1);
        }

        List<Map.Entry<Nation, Integer>> finalCounts = new ArrayList<>(counts.entrySet());

        // shuffle removes name bias if two nations have equal score
        Collections.shuffle(finalCounts);
        finalCounts.sort(Comparator.comparingInt(Map.Entry::getValue));

        List<Nation> winners = getWinners(finalCounts);
        DynamicRegistryManager registries = Objects.requireNonNull(handler.getPlayer().getServer()).getRegistryManager();
        Dialog returnMessage = createReturnDialog(registries, winners);

        handler.getPlayer().openDialog(RegistryEntry.of(returnMessage));

        FORMS_IN_PROGRESS.remove(handler.getPlayer().getUuid());
    }

    private static List<Nation> getWinners(List<Map.Entry<Nation, Integer>> finalCounts) {
        List<Nation> winners = new ArrayList<>();
        int winnerCount = finalCounts.getLast().getValue();
        for (Map.Entry<Nation, Integer> count : finalCounts) {
            if (count.getValue() == winnerCount) {
                winners.add(count.getKey());
            }
        }

        return winners;
    }

    private static Dialog createReturnDialog(DynamicRegistryManager registries, List<Nation> winners) {
        List<DialogBody> body = new ArrayList<>();

        body.add(
                new PlainMessageDialogBody(
                        Text.translatable("tnscore.nations.quiz.result.subtitle"),
                        PlainMessageDialogBody.DEFAULT_WIDTH
                )
        );

        winners.forEach(winner -> {
            body.add(
                    new PlainMessageDialogBody(
                            Text.translatable(
                                    "tnscore.nations.quiz.result.winner",
                                    winner.getTitle(),
                                    winner.getDescription()
                            ),
                            PlainMessageDialogBody.DEFAULT_WIDTH
                    )
            );
        });

        return new NoticeDialog(
                new DialogCommonData(
                        Text.translatable("tnscore.nations.quiz.result.title"),
                        Optional.empty(),
                        false,
                        false,
                        AfterAction.CLOSE,
                        body,
                        List.of()
                ),
                new DialogActionButtonData(
                        new DialogButtonData(
                                Text.translatable("tnscore.gui.ok"),
                                DialogButtonData.DEFAULT_WIDTH
                        ),
                        Optional.of(new SimpleDialogAction(
                                new ClickEvent.ShowDialog(
                                        NationsManager.getEntry(registries, NationsManager.JOIN_NATION_DIALOG)
                                )
                        ))
                )
        );
    }
}