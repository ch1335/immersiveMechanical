package com.chen1335.immersiveMechanical.util;

import com.chen1335.immersiveMechanical.ImmersiveMechanical;
import com.chen1335.immersiveMechanical.client.IMClient;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@EventBusSubscriber(modid = ImmersiveMechanical.MODID)
public class SimpleSchedule {
    @SubscribeEvent
    public static void serverSchedule(ServerTickEvent.Post event) {
        SimpleSchedule.update(Dist.DEDICATED_SERVER);
    }

    @SubscribeEvent
    public static void clientSchedule(ClientTickEvent.Post event) {
        SimpleSchedule.update(Dist.CLIENT);
    }


    private static final Map<Dist, List<Schedule>> DIST_SCHEDULES = ImmutableMap.of(
            Dist.CLIENT, new ArrayList<>(),
            Dist.DEDICATED_SERVER, new ArrayList<>()
    );

    private static final Map<Dist, List<Schedule>> DIST_SCHEDULES_TO_ADD = ImmutableMap.of(
            Dist.CLIENT, new ArrayList<>(),
            Dist.DEDICATED_SERVER, new ArrayList<>()
    );

    public static void addSchedule(Level level, Schedule schedule) {
        if (level.isClientSide) {
            IMClient.submitTask(() -> DIST_SCHEDULES_TO_ADD.get(Dist.CLIENT).add(schedule));
        } else {
            ((ServerLevel) level).getServer().submit(() -> DIST_SCHEDULES_TO_ADD.get(Dist.DEDICATED_SERVER).add(schedule));
        }
    }

    public static void addSchedule(Dist dist, Schedule schedule) {
        if (dist == Dist.CLIENT) {
            IMClient.submitTask(() -> DIST_SCHEDULES_TO_ADD.get(Dist.CLIENT).add(schedule));
        } else {
            MinecraftServer currentServer = ServerLifecycleHooks.getCurrentServer();
            if (currentServer != null) {
                currentServer.submit(() -> DIST_SCHEDULES_TO_ADD.get(Dist.DEDICATED_SERVER).add(schedule));
            }
        }
    }

    private static void update(Dist dist) {
        DIST_SCHEDULES.get(dist).addAll(DIST_SCHEDULES_TO_ADD.get(dist));
        DIST_SCHEDULES_TO_ADD.get(dist).clear();
        Iterator<Schedule> iterator = DIST_SCHEDULES.get(dist).iterator();
        while (iterator.hasNext()) {
            Schedule schedule = iterator.next();
            schedule.tick();
            if (schedule.finished()) {
                schedule.run();
                iterator.remove();
            }
        }
    }

    public abstract static class Schedule {
        public int time = 0;

        public abstract boolean finished();

        public void tick() {
            time++;
        }

        public abstract void run();
    }

    public static class Wait extends Schedule {

        public Wait(Runnable runnable, int waitTick) {
            timeLeft = waitTick;
            this.runnable = runnable;
        }

        private int timeLeft = 0;
        private final Runnable runnable;

        @Override
        public boolean finished() {
            return timeLeft <= 0;
        }

        @Override
        public void tick() {
            timeLeft--;
            super.tick();
        }

        @Override
        public void run() {
            runnable.run();
        }
    }

    public static class RepeatSchedule extends Schedule {

        private final int runCount;
        private final int timeInterval;
        private final Runnable runnable;

        private int current = 0;

        public RepeatSchedule(int runCount, int timeInterval, Runnable runnable) {
            this.runCount = runCount;
            this.timeInterval = timeInterval;
            this.runnable = runnable;
        }


        @Override
        public void tick() {
            if (time % timeInterval == 0) {
                runnable.run();
                current++;
            }
            super.tick();
        }

        @Override
        public boolean finished() {
            return current >= runCount;
        }

        @Override
        public void run() {

        }
    }
}
