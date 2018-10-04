package web;

import bot.IBGameMaster;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import util.AppUtil;

import java.util.List;

@RestController
public class RESTController
{
    @RequestMapping("/")
    public String index() {
        return "hello world";
    }

    @RequestMapping("room/{id}")
    public String getRoom(@PathVariable int id)
    {
        IBGameMaster gameMaster = IBGameMaster.getInstance();
        List players = gameMaster.getRoom(id);
        return AppUtil.toJson(players);
    }
}
