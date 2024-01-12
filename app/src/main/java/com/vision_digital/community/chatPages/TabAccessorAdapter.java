package com.vision_digital.community.chatPages;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class TabAccessorAdapter extends FragmentPagerAdapter {

    public TabAccessorAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ChatsFragment chatsFragment = new ChatsFragment();
                return chatsFragment;

            case 1:
                DoubtsFragment doubtsFragment = new DoubtsFragment();
                return doubtsFragment;

            case 2:
                InvitationsFragment invitationsFragment = new InvitationsFragment();
                return invitationsFragment;

            default:
                return null;
        }


    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Chats";

            case 1:

                return "Doubts";

            case 2:
                return "Invitations";

            default:
                return null;
        }
    }
}
