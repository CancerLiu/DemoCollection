package com.demo.example.demo.curator_demo;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.utils.CloseableUtils;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class LeaderSelectorMonitor {

    public void leaderSelectorOpt(String connectionInfo, String path, List<CuratorFramework> clients) throws IOException {
        List<LeaderSelectorAdapter> examples = Lists.newArrayList();
        try {
            for (int i = 0; i < clients.size(); i++) {
                CuratorFramework client = clients.get(i);
                LeaderSelectorAdapter selectorAdapter = new LeaderSelectorMonitor().new LeaderSelectorAdapter(client, path, "Client #" + i);
                examples.add(selectorAdapter);
                client.start();
                selectorAdapter.start();
            }
            System.out.println("Press enter/return to quit\n");
            new BufferedReader(new InputStreamReader(System.in)).readLine();
        } finally {
            System.out.println("Shutting down...");
            for (LeaderSelectorAdapter exampleClient : examples) {
                CloseableUtils.closeQuietly(exampleClient);
            }
            for (CuratorFramework client : clients) {
                CloseableUtils.closeQuietly(client);
            }
        }
    }

    class LeaderSelectorAdapter extends LeaderSelectorListenerAdapter implements Closeable {
        private final String name;
        private final LeaderSelector leaderSelector;
        private final AtomicInteger leaderCount = new AtomicInteger();

        public LeaderSelectorAdapter(CuratorFramework client, String path, String name) {
            this.name = name;
            leaderSelector = new LeaderSelector(client, path, this);
            leaderSelector.autoRequeue();
        }

        public void start() {
            leaderSelector.start();
        }

        @Override
        public void close() {
            leaderSelector.close();
        }

        @Override
        public void takeLeadership(CuratorFramework client) throws Exception {
            final int waitSeconds = (int) (5 * Math.random()) + 1;
            System.out.println(name + " is now the leader. Waiting " + waitSeconds + " seconds...");
            System.out.println(name + " has been leader " + leaderCount.getAndIncrement() + " time(s) before.");
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(waitSeconds));
            } catch (InterruptedException e) {
                System.err.println(name + " was interrupted.");
                Thread.currentThread().interrupt();
            } finally {
                System.out.println(name + " relinquishing leadership.\n");
            }
        }
    }
}



