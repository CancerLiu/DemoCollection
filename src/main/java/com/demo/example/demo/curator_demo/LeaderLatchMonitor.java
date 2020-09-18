package com.demo.example.demo.curator_demo;

import com.google.common.collect.Lists;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderLatch;
import org.apache.curator.framework.recipes.leader.LeaderLatchListener;
import org.apache.curator.utils.CloseableUtils;

import java.util.List;

/**
 * Curation提供的选举相关的操作
 * <p>
 * Curator 有两种leader选举的方法,分别是LeaderSelector和LeaderLatch。
 * 前者是所有存活的客户端不间断的轮流做Leader；后者是一旦选举出Leader，除非有客户端挂掉重新触发选举，否则不会交出领导权。
 * <p>
 * LeaderLatch必须调用close()方法才会释放领导权，而对于LeaderSelector，通过LeaderSelectorListener可以对领导权进行控制，
 * 在适当的时候释放领导权，这样每个节点都有可能获得领导权。从而，LeaderSelector具有更好的灵活性和可控性，
 * 建议有LeaderElection应用场景下优先使用LeaderSelector。
 */
public class LeaderLatchMonitor {

    public class LeaderLatchResult {
        /**
         * 被选举出的leaderLatch
         */
        private LeaderLatch currentLeaderLatch;

        /**
         * 被选举出的leaderLatch对应的CuratorFrameWork
         */
        private CuratorFramework currentLeaderClient;

        /**
         * 参加选举的节点，用于之后的关闭，后同
         */
        private List<LeaderLatch> examples;

        private List<CuratorFramework> clients;

        public LeaderLatch getCurrentLeaderLatch() {
            return currentLeaderLatch;
        }

        public LeaderLatchResult setCurrentLeaderLatch(LeaderLatch currentLeaderLatch) {
            this.currentLeaderLatch = currentLeaderLatch;
            return this;
        }

        public CuratorFramework getCurrentLeaderClient() {
            return currentLeaderClient;
        }

        public LeaderLatchResult setCurrentLeaderClient(CuratorFramework currentLeaderClient) {
            this.currentLeaderClient = currentLeaderClient;
            return this;
        }

        public List<LeaderLatch> getExamples() {
            return examples;
        }

        public LeaderLatchResult setExamples(List<LeaderLatch> examples) {
            this.examples = examples;
            return this;
        }

        public List<CuratorFramework> getClients() {
            return clients;
        }

        public LeaderLatchResult setClients(List<CuratorFramework> clients) {
            this.clients = clients;
            return this;
        }
    }

    /**
     * 除非自己断线或主动放弃，否则一直是leader
     */
    public static LeaderLatchMonitor.LeaderLatchResult leaderLatchOpt(String connectionInfo, String path, List<CuratorFramework> clients) throws Exception {
        List<LeaderLatch> examples = Lists.newArrayList();
        //加入leaderLatch进行选举
        for (int i = 0; i < clients.size(); i++) {
            CuratorFramework client = clients.get(i);
            LeaderLatch latch = new LeaderLatch(client, path, "Client #" + i);
            latch.addListener(new LeaderLatchListener() {

                @Override
                public void isLeader() {
                    // TODO Auto-generated method stub
                    System.out.println("I am Leader");
                }

                @Override
                public void notLeader() {
                    // TODO Auto-generated method stub
                    System.out.println("I am not Leader");
                }
            });
            examples.add(latch);
            client.start();
            //一旦启动，LeaderLatch会和其它使用相同latch path的其它LeaderLatch交涉，然后其中一个最终会被选举为leader
            latch.start();
        }

        //得到选举结果
        Thread.sleep(1000);
        LeaderLatch currentLeader = null;
        for (LeaderLatch latch : examples) {
            if (latch.hasLeadership()) {
                currentLeader = latch;
            }
        }
        int index = Integer.valueOf(currentLeader.getId().replace("Client #", ""));

        return new LeaderLatchMonitor().new LeaderLatchResult()
                .setClients(clients)
                .setExamples(examples)
                .setCurrentLeaderLatch(currentLeader)
                .setCurrentLeaderClient(clients.get(index));
    }

    public static void leaderLatchClose(LeaderLatchMonitor.LeaderLatchResult result) {
        try {
            System.out.println("关闭LeaderLatch选举");
        } finally {
            for (LeaderLatch latch : result.getExamples()) {
                if (null != latch.getState())
                    CloseableUtils.closeQuietly(latch);
            }
            for (CuratorFramework client : result.getClients()) {
                CloseableUtils.closeQuietly(client);
            }
        }
    }


}
