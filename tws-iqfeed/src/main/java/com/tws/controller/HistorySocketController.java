package com.tws.controller;

import com.google.gson.Gson;
import com.tws.iqfeed.netty.Level1Socket;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

/**
 * Created by admin on 1/30/2016.
 */

@RestController
public class HistorySocketController {

    private Gson gson = new Gson();

    @RequestMapping(value = "/lookup", method = RequestMethod.GET)
    public @ResponseBody String lookup(){
//        Cmd cmd = gson.fromJson(payload, Cmd.class);
//        Level1Socket.send(cmd.getCommand());
        return "";
    }

}
