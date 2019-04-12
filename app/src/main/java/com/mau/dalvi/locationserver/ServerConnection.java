package com.mau.dalvi.locationserver;


import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class ServerConnection {

    private static final String TAG = "ServerConnection";

    private String serverIP = "195.178.227.53";
    private int port = 7117;
    private InetAddress serverAddress;
    private Socket socket;
    private DataInputStream dis;
    private DataOutputStream dos;
    private Controller controller;
    private String id;
    private boolean sendingLocation;


    public ServerConnection(Controller controller) throws IOException {
        this.controller = controller;
        Thread thread = new ServerListener();
        thread.start();


    }

    public void setController(Controller controller) {
        this.controller = controller;
    }


    //Started from new Thread in Controller
    public void startRunning() {
        try {
            connectToServer();

        } catch (IOException io) {
            io.printStackTrace();
        }
    }

    // Attempts to connect to server
    private void connectToServer() throws IOException {
        socket = new Socket(InetAddress.getByName(serverIP), port);
        Log.d(TAG, "connectToServer: connected to " + socket.getInetAddress().getHostName());

        InputStream is = socket.getInputStream();
        DataInputStream dis = new DataInputStream(is);
        OutputStream os = socket.getOutputStream();
        DataOutputStream dos = new DataOutputStream(os);

    }

    public class ServerListener extends Thread {
        public void run() {
            InputStream is = null;
            try {
                socket = new Socket(serverIP, port);
                is = socket.getInputStream();
                dis = new DataInputStream(is);

                OutputStream os = socket.getOutputStream();
                dos = new DataOutputStream(os);

                readFromServer();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void readFromServer() throws IOException {


        while (!socket.isClosed()) {
            try {
                String messageFromServer = dis.readUTF();
                JSONObject jsonObject = new JSONObject(messageFromServer);
                String type = jsonObject.getString("type");
                Log.d(TAG, "readFromServer: type = " + type);

                switch (type) {
                    case "register":
                        parseJsonRegister(jsonObject);
                        break;
                    case "unregister":
                        parseJsonUnregister(jsonObject);
                        break;
                    case "members":
                        parseJsonMembers(jsonObject);
                        break;
                    case "groups":
                        parseGroups(jsonObject);
                        break;
                    case "location":
                        parseJsonLocation(jsonObject);
                        break;
                    case "locations":
                        parseJsonOtherLocations(jsonObject);
                        break;
                    case "exception":
                        controller.showMessage(jsonObject.toString());
                        break;


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }


    //Get and see all existing groups from server
    public String existingGroups() {
        String messageFromServer;

        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("type", "groups");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        messageFromServer = jsonObject.toString();
        return messageFromServer;
    }

    public void getAllGroups() {
        Thread thread = new getGroupsFromServer();
        thread.start();
    }

    public class getGroupsFromServer extends Thread {
        public void run() {

            if (socket != null) {
                try {
                    dos.writeUTF(existingGroups());
                    dos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void parseGroups(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("groups");
            ArrayList<String> groupsArray = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                String test = jsonArray.getString(i);
                JSONObject jsonObject1 = new JSONObject(test);
                groupsArray.add(jsonObject1.getString("group"));
            }
            controller.setListView(groupsArray);
            Log.d("ArrayList", "Setting ListView with: " + groupsArray.size() + " elements");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //Get all members from a certain group
    private String groupMembers(String groupName) {
        String messageMembers;
        JSONObject item = new JSONObject();
        try {
            item.put("type", "members");
            item.put("group", groupName);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        messageMembers = item.toString();
        return messageMembers;
    }

    private void parseJsonMembers(JSONObject jsonObject) {
        try {
            String groupName = jsonObject.getString("group");
            JSONArray jsonArray = jsonObject.getJSONArray("members");
            String[] members = new String[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                String memberJson = jsonArray.getString(i);
                JSONObject jsonObjectMember = new JSONObject(memberJson);
                members[i] = jsonObjectMember.getString("member");
            }
           // controller.setMembers(groupName, members);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    //Register a new group to the server
    private String registerGroup(String group, String member) {
        String messageRegister;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "register");
            jsonObject.put("group", group);
            jsonObject.put("member", member);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        messageRegister = jsonObject.toString();
        return messageRegister;
    }

    void registerNewGroup(String group, String name) {
        Thread thread = new RegisterGroup();
        ((RegisterGroup) thread).setStrings(group, name);
        thread.start();
    }

    public class RegisterGroup extends Thread {
        String groupName;
        String name;

        void setStrings(String groupName, String name) {
            this.groupName = groupName;
            this.name = name;
        }

        public void run() {
            try {
                dos.writeUTF(registerGroup(groupName, name));
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJsonRegister(JSONObject jsonObject) {
        try {
            id = jsonObject.getString("id");
            sendingLocation = true;
            Thread thread = new SendLocationThread();
            thread.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    // Unregister from a group
    private String unregisterGroup() {
        String messageUnregister;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "unregister");
            jsonObject.put("ID", id);
            Log.d(TAG, "unregisterGroup: my id" + id);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        messageUnregister = jsonObject.toString();
        return messageUnregister;
    }

    void unregisterGroupOnServer() {
        Thread thread = new UnregisterGroup();
        thread.start();
    }

    public class UnregisterGroup extends Thread {

        public void run() {
            try {
                dos.writeUTF(unregisterGroup());
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void parseJsonUnregister(JSONObject jsonObject) {
        Log.d(TAG, "parseJsonUnregister: " + jsonObject.toString());
        //controller.showMessage(jsonObject.toString());
    }


    //Others locations
    private void parseJsonOtherLocations(JSONObject jsonObject) {
        try {
            JSONArray jsonArray = jsonObject.getJSONArray("location");
            Members[] memberInfo = new Members[jsonArray.length()];
            for (int i = 0; i < jsonArray.length(); i++) {
                String memberJson = jsonArray.getString(i);
                JSONObject jsonObjectMember = new JSONObject(memberJson);
                String memberName = jsonObjectMember.getString("member");
                double memberLongitude = jsonObjectMember.getDouble("longitude");
                double memberLatitude = jsonObjectMember.getDouble("latitude");
                memberInfo[i] = new Members(memberName, memberLongitude, memberLatitude);
            }
            Log.d(TAG, "Locations from server: " + jsonObject.toString());
            controller.setGroupLocations(memberInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    void requestGroupInfo(String groupName) {
        Thread thread = new RequestGroupInfoFromServer(groupName);
        thread.start();
    }

    public class RequestGroupInfoFromServer extends Thread {
        private String groupName;

        RequestGroupInfoFromServer(String groupName) {
            this.groupName = groupName;
        }

        public void run() {
            try {
                dos.writeUTF(groupMembers(groupName));
                dos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    //Send my location to the server
    private String sendLocation(double latitude, double longitude) {
        String messageLocation;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("type", "location");
            jsonObject.put("id", id);
            jsonObject.put("longitude", "" + longitude);
            jsonObject.put("latitude", "" + latitude);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        messageLocation = jsonObject.toString();
        return messageLocation;
    }

    private class SendLocationThread extends Thread {

        public void run() {
            while (sendingLocation) {
                double latitude = controller.getMyLatitude();
                double longitude = controller.getMyLongitude();
                try {
                    dos.writeUTF(sendLocation(latitude, longitude));
                    dos.flush();
                    sleep(20000);
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void parseJsonLocation(final JSONObject jsonObject) {
        Log.d(TAG, "here bananas" + jsonObject.toString() + id);
    }

    void stopSendLocation() {
        sendingLocation = false;
    }


    //Closes the streams and socket
    private void closeCrap() {
        Log.d(TAG, "closeCrap: closing stuff");
        if (!socket.isClosed()) {
            try {
                socket.close();
            } catch (IOException io) {
                io.printStackTrace();
            }
        }
    }


}
