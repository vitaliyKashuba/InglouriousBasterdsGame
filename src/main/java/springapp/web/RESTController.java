package springapp.web;

import lombok.extern.slf4j.Slf4j;
import net.glxn.qrgen.javase.QRCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import springapp.game.BasicGameMaster;
import springapp.game.EGame;
import springapp.game.Player;
import springapp.game.RoomsKeeper;
import springapp.ibGame.IBGameMaster;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springapp.ibGame.Teammate;
import springapp.mafiaGame.MafiaGameMaster;
import springapp.spyfallGame.SpyfallGameMaster;
import util.AppUtil;
import util.Convertor;
import util.Randomizer;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
public class RESTController {
    @Autowired
    private IBGameMaster ibGameMaster; // = IBGameMaster.getInstance();

    @Autowired
    private SpyfallGameMaster spyfallGameMaster;

    @Autowired
    private MafiaGameMaster mafiaGameMaster;

    @Autowired
    private RoomsKeeper roomsKeeper;

    @CrossOrigin
    @RequestMapping(value = "join/{roomId}", method = RequestMethod.POST)
    public ResponseEntity join(@PathVariable int roomId, @RequestBody String data) {
        JSONObject jsonObject = new JSONObject(data);
        String playerName = jsonObject.getString("name");
        int playerId = Randomizer.getRandomPlayerId();

        //        ibGameMaster.changeStatus(playerId, IBPlayer.Status.JOINREQUEST);                                               // useless in springApplication.web api ?
        //        ibGameMaster.removeOldRoomIfExist(playerId);                                                                    // useless in springApplication.web api ?

        BasicGameMaster master;
        EGame game;

        try {
            game = roomsKeeper.getGameByRoomId(roomId);

            switch (game)                                   // TODO move to gneral master ?
            {
                case INGLORIOUS_BASTERDS:
                    master = ibGameMaster;
                    break;
                case SPYFALL:
                    master = spyfallGameMaster;
                    break;
                case MAFIA:
                    master = mafiaGameMaster;
                    break;
                default:
                    log.error("default switch in RESTController.join");
                    return new ResponseEntity(HttpStatus.BAD_REQUEST);
            }

            master.join(playerId, playerName, roomId, Player.ClientType.WEB);
        } catch (NumberFormatException e) {
            return new ResponseEntity<>("Enter valid room number", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Room not exist", HttpStatus.BAD_REQUEST);
        }

        HashMap<String, Integer> resp = new HashMap<>();
        resp.put("id", playerId);
        resp.put("game", game.getCode());

        return new ResponseEntity<>(Convertor.toJson(resp), HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "setCharacter", method = RequestMethod.POST)
    public ResponseEntity setCharacter(@RequestBody String data) {
        JSONObject jsonObject = new JSONObject(data);
        String character = jsonObject.getString("character");
        int playerId = jsonObject.getInt("id");

        System.out.println(playerId + " " + character);

        ibGameMaster.setCharacter(playerId, character);
        //        ibGameMaster.changeStatus(playerId, IBPlayer.Status.READY);                                               // useless in web api ?

        return AppUtil.responce200OK();
    }

    @RequestMapping("debug")
    public void debug(HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        QRCode.from("Hello World").stream().writeTo(response.getOutputStream());
    }

    /**
     * test'n'debug
     */
    @CrossOrigin
    @RequestMapping("getRandomIbTeammates")
    public String getRandomIbTeammates() {
        List<Teammate> teammates = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            teammates.add(Randomizer.getRandomIbTreammate());
        }
        return Convertor.toJson(teammates);
    }

    /**
     * test'n'debug
     */
    @CrossOrigin
    @RequestMapping("getRandomSpyfallLocations")
    public String getRandomSpyfallLocations() {
        return Convertor.toJson(spyfallGameMaster.getAllLocations().subList(0, 15));
    }

}
