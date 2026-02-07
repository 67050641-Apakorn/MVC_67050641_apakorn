package Controller;

import Model.*;
import View.*;

public class Manage {
    private ClaimDatabase db;
    public OfficialView officialView;

    public Manage() {
        db = new ClaimDatabase();
    }

    public Claim searchClaim(String id) {
        return db.findById(id);
    }

    public void approveClaim(String id) {
        Claim c = db.findById(id);
        if (c != null) {
            c.approve();
            if (officialView != null) officialView.refreshList();
        }
    }

    public String getAllClaimsData() {
        StringBuilder sb = new StringBuilder();
        for (Claim c : db.getAllClaims()) {
            sb.append(c).append("\n");
        }
        return sb.toString();
    }

    public String getSummaryReport() {
        int low = 0, gen = 0, high = 0;
        double totalPay = 0;

        for (Claim c : db.getAllClaims()) {
            if (c.isApproved()) {
                totalPay += c.getCompensationAmount();
                if (c instanceof LowIncomeClaim) low++;
                else if (c instanceof GeneralClaim) gen++;
                else if (c instanceof HighIncomeClaim) high++;
            }
        }

        return String.format(
            "=== Summary Report ===\n" +
            "Low Income Approved: %d\n" +
            "General Approved: %d\n" +
            "High Income Approved: %d\n" +
            "----------------------\n" +
            "Total Compensation: %.2f Baht",
            low, gen, high, totalPay
        );
    }
}