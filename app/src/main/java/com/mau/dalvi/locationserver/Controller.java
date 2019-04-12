package com.mau.dalvi.locationserver;


import android.util.Log;

import com.google.android.gms.maps.SupportMapFragment;

import java.io.IOException;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class Controller {

    private MainActivity mainActivity;
    private MyLocationHandler myLocationHandler;
    private ServerConnection serverConnection;
    private MapMaster mapMaster;
    private LoginFragment loginFragment;
    private GroupsOnServerFragment groupsOnServerFragment;
    private SupportMapFragment mapFragment;
    private HomePageFragment homePageFragment;
    private NewGroupFragment newGroupFragment;

    private String username = "";
    private double longitude = 0;
    private double latitude = 0;
    private String[] groups;
    private String currentFrag = "";
    private boolean partOfGroup = false;
    private String currentGroup = "";

    public Controller(MainActivity mainActivity) throws IOException {
        this.mainActivity = mainActivity;
        myLocationHandler = new MyLocationHandler(mainActivity);
        myLocationHandler.setController(this);
        myLocationHandler.getMyLocation();
        serverConnection = new ServerConnection(this);
        // connectToServer();
        initComponents();

    }


    public void initComponents() throws IOException {

        serverConnection = new ServerConnection(this);
        serverConnection.setController(this);
        mapFragment = new SupportMapFragment();
        mapMaster = new MapMaster(mapFragment);
        groupsOnServerFragment = new GroupsOnServerFragment();
        loginFragment = new LoginFragment();
        homePageFragment = new HomePageFragment();
        newGroupFragment = new NewGroupFragment();

        mainActivity.setFragment(loginFragment, true);
        loginFragment.setController(this);

    }

    //Getters and Setters

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getMyLatitude() {
        return latitude;
    }

    public double getMyLongitude() {
        return longitude;
    }

    public void setMyLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setMyLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setListView(final ArrayList<String> groups) {
        this.groups = new String[groups.size()];
        for (int i = 0; i < groups.size(); i++) {
            this.groups[i] = groups.get(i);
        }
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                groupsOnServerFragment.arrayAdapter(groups);
            }
        });
    }

    public String[] getGroupsArray() {
        return groups;
    }


    //Buttons in alphabetical order

    public void btnAddGroup() {
        mainActivity.setFragment(newGroupFragment, true);
        newGroupFragment.setController(this);
    }

    public void btnBackClicked() {
        mainActivity.setFragment(homePageFragment, true);
        homePageFragment.setController(this);
    }

    public void btnFindMeClicked() {
        latitude = myLocationHandler.getLatitude();
        longitude = myLocationHandler.getLongitude();
    }

    public void btnMapClicked() {
        mainActivity.setFragment(mapFragment, true);
        mapMaster.setController(this);
        mapMaster.updateMap();
        Log.d(TAG, "btnMapClicked: my controller location" + getMyLongitude() + " " + getMyLatitude());
    }

    public void btnRegisterGroupClicked(String groupName) {
        serverConnection.registerNewGroup(groupName, username);
        Log.d(TAG, "btnRegisterGroupClicked: " + groupName + " " + username);
        mainActivity.setFragment(groupsOnServerFragment, true);
        groupsOnServerFragment.setController(this);
        partOfGroup = true;

    }

    public void btnSignInClicked() {
        mainActivity.setFragment(homePageFragment, true);
        homePageFragment.setController(this);
    }

    public void btnViewGroupsClicked() throws IOException {
        mainActivity.setFragment(groupsOnServerFragment, true);
        groupsOnServerFragment.setController(this);
        getAllGroups();
    }


    public void groupJoinedClicked(String groupname, String username) {
        Log.d(TAG, "groupJoinedClicked: apples " + groupname);
        if (!partOfGroup) {
            serverConnection.registerNewGroup(groupname, username);
            partOfGroup = true;

        } else {
            unRegisterFromGroup();
            partOfGroup = false;

        }
    }

    public void unRegisterFromGroup() {

        serverConnection.unregisterGroupOnServer();
        serverConnection.stopSendLocation();
        partOfGroup = false;
        currentGroup = "";
        mapMaster.setMarkers(new Members[0]);
        mapMaster.updateMap();
    }

    public void getGroupInfo(String groupname) {
        Log.d(TAG, "getGroupInfo: banana " + groupname);
        serverConnection.requestGroupInfo(groupname);

    }


    public void getAllGroups() {
        serverConnection.getAllGroups();
    }

    public void showMessage(String message) {
        groupsOnServerFragment.showMessage(message);
    }

    void setGroupLocations(Members[] memberInfo) {
        mapMaster.setMarkers(memberInfo);
        if (currentFrag.equals("mapFragment")) {
            runOnUIThread(new Runnable() {
                @Override
                public void run() {
                    mapMaster.updateMap();
                }
            });
        }
    }

    /*public void setGroupMembers(final String groupName, final String[] members) {
        runOnUIThread(new Runnable() {
            @Override
            public void run() {
                detailedGroupFragment.setMembers(groupName, members);
                if(groupName.equals(currentGroup)){
                    detailedGroupFragment.ableToJoinGroup(false);
                }else{
                    detailedGroupFragment.ableToJoinGroup(true);
                }
            }
        });
    }*/

    private void runOnUIThread(Runnable runnable) {
        groupsOnServerFragment.runOnUIThread(runnable);
    }

    //Thread reading information from server, started on btnStart (view groups)
    class ReadTask implements Runnable {

        @Override
        public void run() {
            try {

                serverConnection.readFromServer();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

