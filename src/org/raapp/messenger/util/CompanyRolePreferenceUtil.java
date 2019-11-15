package org.raapp.messenger.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.raapp.messenger.OfficeAppConstants;
import org.raapp.messenger.client.datamodel.CompanyRoleDTO;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public final class CompanyRolePreferenceUtil {

    public static void saveCompanyRoleList(Context context, List<CompanyRoleDTO> companyRoleDTOS) {
        SharedPreferences appSharedPrefs = context.getSharedPreferences(OfficeAppConstants.RA_PREFERENCES, MODE_PRIVATE);
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();
        String json = gson.toJson(companyRoleDTOS);
        prefsEditor.putString("companyRoles", json);
        prefsEditor.apply();
    }

    public static List<CompanyRoleDTO> getCompanyRolList(Context context) {
        SharedPreferences appSharedPrefs = context.getSharedPreferences(OfficeAppConstants.RA_PREFERENCES, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = appSharedPrefs.getString("companyRoles", "");
        Type type = new TypeToken<List<CompanyRoleDTO>>(){}.getType();
        return gson.fromJson(json, type);
    }

    public static void addCompanyRole(Context context, CompanyRoleDTO companyRoleDTO) {
        List<CompanyRoleDTO> companyRoleDTOS = getCompanyRolList(context);
        if (companyRoleDTOS == null) {
            companyRoleDTOS = new ArrayList<>();
        }
        companyRoleDTOS.add(companyRoleDTO);
        saveCompanyRoleList(context, companyRoleDTOS);
    }

}
