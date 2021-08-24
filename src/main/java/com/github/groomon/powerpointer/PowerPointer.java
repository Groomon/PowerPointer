package com.github.groomon.powerpointer;

import org.takes.Response;
import org.takes.Take;
import org.takes.facets.fork.FkRegex;
import org.takes.facets.fork.TkFork;
import org.takes.http.Exit;
import org.takes.http.FtBasic;
import org.takes.rs.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class PowerPointer {

    private static Robot robot;

    public static void main(String... args) throws IOException, AWTException {
        if(System.console() == null) System.exit(0);

        robot = new Robot();

        System.out.println("Starting webserver");
        new FtBasic(
                new TkFork(
                        new FkRegex("/", (Take) request -> buildCoreResponse(new RsWithBody(PowerPointer.class.getResourceAsStream("/index.html")))),
                        new FkRegex("/call/ping", (Take) request -> {
                            System.out.println("ping");
                            return buildCoreResponse(new RsWithStatus(200));
                        }),
                        new FkRegex("/call/left", (Take) request -> {
                            System.out.println("LEFT");
                            robot.keyPress(KeyEvent.VK_LEFT);
                            Thread.sleep(50);
                            robot.keyRelease(KeyEvent.VK_LEFT);
                            return buildCoreResponse(new RsWithStatus(200));
                        }),
                        new FkRegex("/call/right", (Take) request -> {
                            System.out.println("RIGHT");
                            robot.keyPress(KeyEvent.VK_RIGHT);
                            Thread.sleep(50);
                            robot.keyRelease(KeyEvent.VK_RIGHT);
                            return buildCoreResponse(new RsWithStatus(200));
                        })
                ), 80
        ).start(Exit.NEVER);
    }

    public static Response buildCoreResponse(Response res) {
        return new RsWithHeader(new RsWithoutHeader(res, "Access-Control-Allow-Origin"), "Access-Control-Allow-Origin", "*");
    }
}