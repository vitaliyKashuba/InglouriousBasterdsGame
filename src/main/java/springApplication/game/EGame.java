package springApplication.game;

import lombok.Getter;

public enum EGame
{
    INGLORIOUS_BASTERDS(0),
    SPYFALL(1),
    MAFIA(2);

    EGame(int code)
    {
        this.code = code;
    }

    @Getter
    private int code;   // because js enums comparing like shit
}
