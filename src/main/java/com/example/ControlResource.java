package com.example;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/")
public class ControlResource {

    private static final Logger log = Logger.getLogger(ControlResource.class);

    volatile Status status = Status.STOPPED;

    @RestClient
    Client client;

    boolean started = false;

    @PostConstruct
    public void setUp() {
        Thread sendingThread = new Thread(() -> {
            try {
                while (true) {
                    synchronized(ControlResource.this) {
                        while (!started) {
                            ControlResource.this.wait();
                        }
                    }
                    Thread.sleep(50L);
                    try {
                        client.get();
                    } catch (Exception any) {
                        log.error("Failed to connect to the remote service", any);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        sendingThread.setDaemon(true);
        sendingThread.start();
    }

    @GET
    public String hello(@QueryParam("requestCount") @DefaultValue("100") int requestCount) {
        for (int i = 0; i < requestCount; i++) {
            client.get();
        }
        return "All done!";
    }

    @POST
    @Path("/start")
    public void start() {
        synchronized (this) {
            started = true;
            this.notify();
            status = Status.SENDING;
        }
    }

    @POST
    @Path("/stop")
    public void stop() {
        synchronized (this) {
            started = false;
            status = Status.STOPPED;
        }
    }

    @GET
    @Path("/status")
    public StatusInfo status() {
        return new StatusInfo(status);
    }

    public static class StatusInfo {
        public Status status;

        public StatusInfo(Status status) {
            this.status = status;
        }
    }

    public enum Status {
        STOPPED,
        SENDING
    }
}