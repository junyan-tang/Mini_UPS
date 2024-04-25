package edu.duke.ece568.mini_ups.service;

import java.util.LinkedList;
import java.util.Queue;

import edu.duke.ece568.mini_ups.protocol.upsToAmazon.AmazonUps.AOrderATruck;

public class CommandStore {
    public static final Queue<AOrderATruck> pendingOrders = new LinkedList<>();
    public static final Queue<DestStruct> pendingDest = new LinkedList<>();
}
