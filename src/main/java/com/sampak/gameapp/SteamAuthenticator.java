package com.sampak.gameapp;

import org.openid4java.association.AssociationException;
import org.openid4java.consumer.ConsumerException;
import org.openid4java.consumer.ConsumerManager;
import org.openid4java.consumer.VerificationResult;
import org.openid4java.discovery.DiscoveryException;
import org.openid4java.discovery.DiscoveryInformation;
import org.openid4java.discovery.Identifier;
import org.openid4java.message.AuthRequest;
import org.openid4java.message.MessageException;
import org.openid4java.message.ParameterList;
import org.openid4java.message.ax.FetchRequest;


import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SteamAuthenticator {
    private static final String STEAM_OPENID = "http://steamcommunity.com/openid";
    private final Pattern STEAM_REGEX = Pattern.compile("(\\d+)");
    private final ConsumerManager consumerManager;
    private DiscoveryInformation discovered;

    public SteamAuthenticator() {
        consumerManager = new ConsumerManager();
        consumerManager.setMaxAssocAttempts(0);

        try {
            discovered = consumerManager.associate(consumerManager.discover(STEAM_OPENID));
        } catch (DiscoveryException e) {
            discovered = null;
            e.printStackTrace();
        }
    }


    public String authenticate(String redirectURL) {
        if (discovered == null) {
            return null;
        }

        try {
            AuthRequest authReq = consumerManager.authenticate(this.discovered, redirectURL);
            return authReq.getDestinationUrl(true);
        } catch (MessageException | ConsumerException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String verify(String receivingUrl, Map responseMap) {
        if (this.discovered == null) {
            return null;
        }
        ParameterList responseList = new ParameterList(responseMap);
        try {
            VerificationResult verification = consumerManager.verify(receivingUrl, responseList, this.discovered);
            Identifier verifiedId = verification.getVerifiedId();
            if (verifiedId != null) {
                String id = verifiedId.getIdentifier();
                Matcher matcher = STEAM_REGEX.matcher(id);
                if (matcher.find()) {
                    System.out.println();
                    return matcher.group(1);
                }
            }
        } catch (MessageException | DiscoveryException | AssociationException e) {
            e.printStackTrace();
        }
        return null;
    }
}
