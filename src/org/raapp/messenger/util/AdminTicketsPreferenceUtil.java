package org.raapp.messenger.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.raapp.messenger.OfficeAppConstants;
import org.raapp.messenger.client.datamodel.AdminTicketDTO;
import org.raapp.messenger.logging.Log;

import java.lang.reflect.Type;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public final class AdminTicketsPreferenceUtil {

    public static void saveAdminTicketsList(Context context, List<AdminTicketDTO> adminTicketsDTOS) {
        SharedPreferences appSharedPrefs = context.getSharedPreferences(OfficeAppConstants.RA_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(adminTicketsDTOS);
        prefsEditor.putString("adminTickets", json);
        prefsEditor.apply();
    }

    public static List<AdminTicketDTO> getAdminTicketsList(Context context) {
        SharedPreferences appSharedPrefs = context.getSharedPreferences(OfficeAppConstants.RA_PREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("adminTickets", "[]");
        Type type = new TypeToken<List<AdminTicketDTO>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public static AdminTicketDTO findAdminTicketByThread(Context context, long threadID){
        for (AdminTicketDTO adminTicket: getAdminTicketsList(context)) {
            if(adminTicket.getThreadID() == threadID){
                return adminTicket;
            }
        }
        return  null;
    }

    private static int findTicketIndexByUUID(Context context, String UUID){
        List<AdminTicketDTO> list = getAdminTicketsList(context);
        for (AdminTicketDTO adminTicket: list) {
            if(adminTicket.getUuid() != null && adminTicket.getUuid().equals(UUID)){
                return list.indexOf(adminTicket);
            }
        }
        return  -1;
    }

    public static void removeTicketByUUID(Context context, String UUID){
        List<AdminTicketDTO> adminTicketsDTOS = getAdminTicketsList(context);
        int index = findTicketIndexByUUID(context, UUID);
        if(index != -1){
            adminTicketsDTOS.remove(index);
            saveAdminTicketsList(context, adminTicketsDTOS);
        }

    }

    public static void addAdminTicket(Context context, AdminTicketDTO adminTicketDTO) {
        List<AdminTicketDTO> adminTicketsDTOS = getAdminTicketsList(context);
        adminTicketsDTOS.add(adminTicketDTO);
        saveAdminTicketsList(context, adminTicketsDTOS);
    }

}
