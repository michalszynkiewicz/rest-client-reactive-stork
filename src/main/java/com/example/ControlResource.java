package com.example;

import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/")
@ApplicationScoped
public class ControlResource {

    private static final Logger log = Logger.getLogger(ControlResource.class);

    volatile Status status = Status.STOPPED;

    @RestClient
    Client client;

    private Thread sendingThread;

    @PostConstruct
    public void setUp() {
        sendingThread = new Thread(() -> {
            try {
                while (true) {
                    synchronized(ControlResource.this) {
                        while (status != Status.SENDING) {
                            ControlResource.this.wait();
                        }
                    }
                    Thread.sleep(50L);
                    try {
                        client.get().onFailure().invoke(e -> log.error("Failed to connect to the remote service", e))
                                .subscribe().with(log::info);
                    } catch (Exception any) {
                        log.error("Failed to connect to the remote service", any);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace(); // this should break the loop and the thread should be finished on interruption
            }
        });
        sendingThread.setDaemon(true);
        sendingThread.start();
    }

    @PreDestroy
    public void tearDown() {
        log.info("shutting down sender!");
        sendingThread.interrupt();
    }

    @GET
    public String hello(@QueryParam("requestCount") @DefaultValue("100") int requestCount) {
        for (int i = 0; i < requestCount; i++) {
            client.get().onFailure().invoke(e -> log.error("Failed to connect to the remot service", e))
                    .subscribe().with(log::info);
        }
        return "All done!";
    }

    @POST
    @Path("/start")
    public void start() {
        synchronized (this) {
            status = Status.SENDING;
            this.notify();
        }
    }

    @POST
    @Path("/stop")
    public void stop() {
        synchronized (this) {
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