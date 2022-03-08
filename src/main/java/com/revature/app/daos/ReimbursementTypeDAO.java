package com.revature.app.daos;

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
public class ReimbursementTypeDAO implements CrudDAO{

    private final String rootSelect = "SELECT * FROM reimbursement_types ";


    @Override
    public void save(Object newObject) {
        return;
    }


    @Override
    public ReimbursementType getById(String id) {
        ReimbursementType reimbursementType = null;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(rootSelect + "WHERE id = ?");
            pstmt.setString(1, id);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                reimbursementType = new ReimbursementType();
                reimbursementType.setId(rs.getString("id"));
                reimbursementType.setType(rs.getString("type"));
            }

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }

        return reimbursementType;
    }


    public ReimbursementType getByName(String type) {
        ReimbursementType reimbursementType = null;

        try (Connection conn = ConnectionFactory.getInstance().getConnection()) {
            PreparedStatement pstmt = conn.prepareStatement(rootSelect + "WHERE type = ?");
            pstmt.setString(1, type);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                reimbursementType = new ReimbursementType();
                reimbursementType.setId(rs.getString("id"));
                reimbursementType.setType(rs.getString("type"));
            }

        } catch (SQLException e) {
            throw new DataSourceException(e);
        }

        return reimbursementType;
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
