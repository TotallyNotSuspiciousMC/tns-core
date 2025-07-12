package com.totallynotsuspicious.core.nations.quiz;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.totallynotsuspicious.core.TNSCore;
import com.totallynotsuspicious.core.nations.Nation;
import com.totallynotsuspicious.core.nations.NationsManager;
import net.minecraft.dialog.type.Dialog;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.StringIdentifiable;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;

public class QuizForm {
    private final Map<QuizQuestion, QuizAnswer> answers = new EnumMap<>(QuizQuestion.class);

    public void addAnswer(QuizQuestion question, QuizAnswer answer) {
        answers.put(question, answer);
    }

    public Collection<QuizAnswer> answers() {
        return this.answers.values();
    }

    static RegistryKey<Dialog> key(String name) {
        return RegistryKey.of(RegistryKeys.DIALOG, TNSCore.id("personality_quiz/" + name));
    }

    public enum QuizQuestion implements StringIdentifiable {
        PLAY_STYLE("play_style"),
        CONFLICT_APPROACH("conflict_approach"),
        PREFERRED_BIOME("preferred_biome"),
        IDEAL_BASE("ideal_base"),
        ATTITUDE("attitude"),
        GAME_SESSION("game_session"),
        QUOTE("quote"),
        FRIENDS_DESCRIPTION("friends_description");

        public static final EnumCodec<QuizQuestion> CODEC = StringIdentifiable.createCodec(QuizQuestion::values);

        private final String name;
        private final RegistryKey<Dialog> questionDialog;

        QuizQuestion(String name) {
            this.name = name;
            this.questionDialog = key("question" + (this.ordinal() + 1));
        }

        @Override
        public String asString() {
            return this.name;
        }

        public void openDialog(ServerPlayerEntity player) {
            NationsManager.openDialog(player, this.questionDialog);
        }

        @Nullable
        public QuizQuestion getNextQuestion() {
            int nextOrdinal = this.ordinal() + 1;
            if (nextOrdinal >= values().length) {
                return null;
            }

            return values()[nextOrdinal];
        }
    }

    public record AnswerPayload(
            QuizAnswer answer
    ) {
        public static final Codec<AnswerPayload> CODEC = Codec.withAlternative(
                RecordCodecBuilder.create(
                        instance -> instance.group(
                                QuizAnswer.CODEC.fieldOf("answer")
                                        .forGetter(AnswerPayload::answer)
                        ).apply(instance, AnswerPayload::new)
                ),
                QuizAnswer.CODEC.xmap(AnswerPayload::new, payload -> payload.answer)
        );
    }

    public enum QuizAnswer implements StringIdentifiable {
        FIDELIS(Nation.FIDELIS, "fidelis_answer"),
        PANDORA(Nation.PANDORA, "pandora_answer"),
        TAURE_ARANIE(Nation.TAURE_ARANIE, "taure_aranie_answer"),
        VAYUNE(Nation.VAYUNE, "vayune_answer");

        public static final EnumCodec<QuizAnswer> CODEC = StringIdentifiable.createCodec(QuizAnswer::values);

        private final Nation nation;
        private final String name;

        QuizAnswer(Nation nation, String name) {
            this.nation = nation;
            this.name = name;
        }

        public Nation nation() {
            return this.nation;
        }

        @Override
        public String asString() {
            return this.name;
        }
    }
}