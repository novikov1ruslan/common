package com.ivygames.common.invitations;

import android.support.annotation.NonNull;

import com.google.android.gms.games.multiplayer.Invitation;
import com.google.android.gms.games.multiplayer.OnInvitationReceivedListener;
import com.ivygames.common.googleapi.ApiClient;
import com.ivygames.common.googleapi.InvitationLoadListener;

import org.commons.logger.Ln;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InvitationManager {

    @NonNull
    private final Set<String> mIncomingInvitationIds = new HashSet<>();
    @NonNull
    private final List<InvitationListener> mInvitationListeners = new ArrayList<>();

    @NonNull
    private final ApiClient mApiClient;

    public InvitationManager(@NonNull ApiClient client) {
        mApiClient = client;
    }

    public void addInvitationListener(@NonNull InvitationListener receiver) {
        mInvitationListeners.add(receiver);
        Ln.d(receiver + " registered as invitation receiver");
    }

    public void removeInvitationReceiver(@NonNull InvitationListener receiver) {
        mInvitationListeners.remove(receiver);
        Ln.d(receiver + " unregistered as invitation receiver");
    }

    public void loadInvitations() {
        if (!mApiClient.isConnected()) {
            Ln.w("API client has to be connected");
            return;
        }

        mApiClient.registerInvitationListener(new OnInvitationReceivedListenerImpl());

        Ln.d("loading invitations...");
        mApiClient.loadInvitations(new InvitationLoadListenerImpl());
    }

    @NonNull
    public Set<String> getInvitationIds() {
        return new HashSet<>(mIncomingInvitationIds);
    }

    private void notifyReceived(@NonNull GameInvitation invitation) {
        for (InvitationListener listener : mInvitationListeners) {
            listener.onInvitationReceived(invitation);
        }
    }

    private void notifyUpdated() {
        for (InvitationListener listener : mInvitationListeners) {
            listener.onInvitationsUpdated(new HashSet<>(mIncomingInvitationIds));
        }
    }

    private class OnInvitationReceivedListenerImpl implements OnInvitationReceivedListener {

        @Override
        public void onInvitationReceived(Invitation invitation) {
            String displayName = invitation.getInviter().getDisplayName();
            Ln.d("received invitation from: " + displayName);

            String invitationId = invitation.getInvitationId();
            mIncomingInvitationIds.add(invitationId);
            notifyReceived(new GameInvitation(displayName, invitationId));
        }

        @Override
        public void onInvitationRemoved(String invitationId) {
            Ln.d("invitationId=" + invitationId + " withdrawn");
            mIncomingInvitationIds.remove(invitationId);
            notifyUpdated();
        }
    }

    private class InvitationLoadListenerImpl implements InvitationLoadListener {

        @Override
        public void onLoaded(@NonNull Collection<GameInvitation> invitations) {
            mIncomingInvitationIds.clear();
            if (invitations.size() > 0) {
                Ln.v("loaded " + invitations.size() + " invitations");
                for (GameInvitation invitation: invitations) {
                    mIncomingInvitationIds.add(invitation.id);
                }
            } else {
                Ln.d("no invitations");
            }
            notifyUpdated();
        }
    }
}
