package org.raapp.messenger.util;

import android.content.Context;

import org.raapp.messenger.client.datamodel.CompanyRoleDTO;
import org.raapp.messenger.logging.Log;

import java.util.List;

public final class RoleUtil {

    public static boolean isAdminInCompany(Context context, String companyId) {
        String role = "";
        List<CompanyRoleDTO> companyRoleDTOS = CompanyRolePreferenceUtil.getCompanyRolList(context);
        if(companyRoleDTOS != null){
            for (CompanyRoleDTO companyRoleDTO: companyRoleDTOS) {
                if (companyRoleDTO.getCompanyId().equals(companyId)) {
                    role = companyRoleDTO.getRole();
                }
            }
            Log.i("Role", "" + role);

        }
        return "admin".equalsIgnoreCase(role);
    }
}
