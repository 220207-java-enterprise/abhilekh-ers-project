package com.revature.app.daos;

import com.revature.app.models.ReimbursementStatus;
import com.revature.app.models.ReimbursementType;
import com.revature.app.util.ConnectionFactory;
import com.revature.app.util.exceptions.DataSourceException;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class ReimbursementStatusDAO implements CrudDAO{


    private final String rootSelect = "SELECT * FROM reimbursement_statuses ";

    @Override
    public void save(Object newObject) {

    }

    @Override
    public ReimbursementStatus getById(String id) {
        ReimbursementStatus reimbursementStatus = null;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(rootSelect + "WHERE id = ?");
            pstmt.setString(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                reimbursementStatus = new ReimbursementStatus();
                reimbursementStatus.setId(rs.getString("id"));
                reimbursementStatus.setStatus(rs.getString("status"));
            }

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }

        return reimbursementStatus;
    }

    @Override
    public List getAll() {
        return null;
    }

    @Override
    public void update(Object updateObject) {

    }

    @Override
    public void deleteById(String Id) {

    }
}
