package springApplication.web;

import org.springframework.beans.factory.annotation.Autowired;
import springApplication.game.IBGameMaster;
import springApplication.game.IBPlayer;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import util.AppUtil;
import util.Randomizer;

import java.util.HashMap;
import java.util.List;

@RestController
public class RESTController
{
    @Autowired
    private IBGameMaster gameMaster; // = IBGameMaster.getInstance();

    @RequestMapping("/")
    public String index() {
        return "hello world";
    }

    /**test*/
    @RequestMapping("room/{id}")
    public String getRoom(@PathVariable int id)
    {
//        IBGameMaster gameMaster = IBGameMaster.getInstance();
        List<IBPlayer> players = gameMaster.getRoom(id);
        return AppUtil.toJson(players);
    }

    /**test*/
    @RequestMapping("players")
    public String getPlayers()
    {
        return AppUtil.toJson(gameMaster.getPlayers());
    }


    @CrossOrigin
    @RequestMapping(value = "join/{roomId}", method = RequestMethod.POST)
    public ResponseEntity join(@PathVariable int roomId, @RequestBody String data)
    {
        JSONObject jsonObject = new JSONObject(data);
        String playerName = jsonObject.getString("name");
        int playerId = Randomizer.getRandomPlayerId();

        System.out.println(playerId + " " + playerName);
        gameMaster.addPlayer(new IBPlayer(playerId, playerName, IBPlayer.ClientType.WEB));

//        gameMaster.changeStatus(playerId, IBPlayer.Status.JOINREQUEST);                                               // useless in springApplication.web api ?
//        gameMaster.removeOldRoomIfExist(playerId);                                                                    // useless in springApplication.web api ?

        try
        {
            gameMaster.enterRoom(playerId, roomId);
        } catch (NumberFormatException e)
        {
            return new ResponseEntity<>("Enter valid room number", HttpStatus.BAD_REQUEST);
        } catch (IllegalArgumentException e)
        {
            return new ResponseEntity<>("Room not exist", HttpStatus.BAD_REQUEST);
        }

//        return AppUtil.responce200OK();
        HashMap<String, Integer> resp = new HashMap<>();
        resp.put("id", playerId);
        return new ResponseEntity<>(AppUtil.toJson(resp),HttpStatus.OK);
    }

    @CrossOrigin
    @RequestMapping(value = "setCharacter", method = RequestMethod.POST)
    public ResponseEntity setCharacter(@RequestBody String data)
    {
        JSONObject jsonObject = new JSONObject(data);
        String character = jsonObject.getString("character");
        int playerId = jsonObject.getInt("id");

        System.out.println(playerId + " " + character);

        gameMaster.setCharacter(playerId, character);
//        gameMaster.changeStatus(playerId, IBPlayer.Status.READY);                                               // useless in springApplication.web api ?

        return AppUtil.responce200OK();
    }
}
