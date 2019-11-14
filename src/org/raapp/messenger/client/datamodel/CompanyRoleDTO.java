package org.raapp.messenger.client.datamodel;

public class CompanyRoleDTO {

    private String companyId;
    private String role;

    public CompanyRoleDTO(String companyId, String role) {
        this.companyId = companyId;
        this.role = role;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
