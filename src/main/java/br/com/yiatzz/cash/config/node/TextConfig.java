package br.com.yiatzz.cash.config.node;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextTemplate;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import static org.spongepowered.api.text.Text.builder;
import static org.spongepowered.api.text.TextTemplate.arg;
import static org.spongepowered.api.text.TextTemplate.of;

@SuppressWarnings("FieldMayBeFinal")
@ConfigSerializable
public class TextConfig {

    private static final TextColor WARNING_COLOR = TextColors.DARK_RED;
    private static final TextColor INFO_COLOR = TextColors.DARK_GREEN;

    @Setting(comment = "Quando o comando é apenas para players e é executado pelo console.")
    private Text playersOnly = builder("Apenas jogadores podem fazer isto!").color(WARNING_COLOR).build();

    @Setting(comment = "Visualização do cash de um jogador")
    private TextTemplate cashInfo = of(
            INFO_COLOR, "Cash de ", TextColors.WHITE, arg("player").optional(), " é ", TextColors.WHITE, "<cash> ", INFO_COLOR, "cashs!"
    );

    @Setting(comment = "Visualização do seu cash")
    private TextTemplate selfCashInfo = of(
            INFO_COLOR, "Você possui", TextColors.WHITE, " <cash>, ", INFO_COLOR, " cashs!"
    );

    @Setting(comment = "Definiu cash pro jogador")
    private TextTemplate defineCash = of(
            INFO_COLOR, "Você definiu ", TextColors.WHITE, " <cash>, ", INFO_COLOR, " cashs para ", arg("nome").optional(), "!"
    );

    @Setting(comment = "Adicionou cash pro jogador")
    private TextTemplate addCash = of(
            INFO_COLOR, "Você adicionou ", TextColors.WHITE, " <cash>, ", INFO_COLOR, " cashs para ", arg("nome").optional(), "!"
    );

    @Setting(comment = "Removeu cash do jogador")
    private TextTemplate removeCash = of(
            INFO_COLOR, "Você removeu ", TextColors.WHITE, " <cash>, ", INFO_COLOR, " cashs de ", arg("nome").optional(), "!"
    );

    @Setting(comment = "Jogador inválido.")
    private Text invalidUser = builder("Jogador inválido.").color(WARNING_COLOR).build();

    @Setting(comment = "Quantia de cash inválida.")
    private Text invalidDouble = builder("Quantia de cash inválida.").color(WARNING_COLOR).build();

    public Text getPlayersOnly() {
        return playersOnly;
    }

    public TextTemplate getCashInfo() {
        return cashInfo;
    }

    public TextTemplate getDefineCash() {
        return defineCash;
    }

    public TextTemplate getAddCash() {
        return addCash;
    }

    public TextTemplate getRemoveCash() {
        return removeCash;
    }

    public TextTemplate getSelfCashInfo() {
        return selfCashInfo;
    }

    public Text getInvalidUser() {
        return invalidUser;
    }

    public Text getInvalidDouble() {
        return invalidDouble;
    }
}
