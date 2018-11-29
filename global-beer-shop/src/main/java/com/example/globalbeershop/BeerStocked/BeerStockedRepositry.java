package com.example.globalbeershop.BeerStocked;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class BeerStockedRepositry {

    @Autowired
    JdbcTemplate jdbcTemplate;

    class BeerStockedRowMapper implements RowMapper<BeerStocked> {

        @Override
        public BeerStocked mapRow(ResultSet rs, int rowNum) throws SQLException {
            BeerStocked beer = new BeerStocked();
            beer.setId(rs.getLong("id"));
            beer.setName(rs.getString("name"));
            beer.setCountry(rs.getString("country"));
            beer.setBrewer(rs.getString("brewer"));
            beer.setType(rs.getString("type"));
            beer.setAbv(rs.getDouble("abv"));

            return beer;
        }

    }

    public List<BeerStocked> findAll(List<String> sort) {

        //checks sorting requirements have been given
        if(sort.size() == 2){

            List<String> validSortOrd = new ArrayList<String>() {{add("ASC"); add("DESC");}};
            List<String> validSortCol = new ArrayList<String>() {{add("country"); add("type");add("abv"); add("brewer"); add("name");}};

            //checks if the requirement args are valid
            if (sort.size() == 2){
                if (!validSortOrd.contains(sort.get(1)) || !validSortCol.contains(sort.get(0))) return null;
            }
            else{
                return jdbcTemplate.query("select * from beerStocked ORDER BY "+sort.get(0)+ " " + sort.get(1), new BeerStockedRowMapper());
            }
        }
        return jdbcTemplate.query("select * from beerStocked", new BeerStockedRowMapper());
    }

    public BeerStocked findById(long id) {
        return jdbcTemplate.queryForObject("select * from beerStocked where id=?", new Object[] { id },
                new BeerStockedRowMapper());
    }


    public List<BeerStocked> findByColumn(List<String> cols, List<Object> vals, List<String> sort){

        /*VALIDATES ARGS
            - At least one col and one val must be given
            - The the number of cols = number of vals
            - If order has 2 items, second item is either "ASC" or "DESC" (respectively)
            - If order has 2 items, the first item is only from an allowed set of columns
         */

        List<String> validSortOrd = new ArrayList<String>() {{add("ASC"); add("DESC");}};
        List<String> validSortCol = new ArrayList<String>() {{add("country"); add("type");add("abv"); add("brewer"); add("name");}};

        if(cols.size() < 1 || vals.size() < 1 || vals.size() != cols.size()){
            return null;
        }
        else if (sort.size() == 2){
            if (!validSortOrd.contains(sort.get(1)) || !validSortCol.contains(sort.get(0))) return null;
        }


        //the basic sql statement and query values for a one specified column
        String sql = "select * from beerStocked where " + cols.get(0) + "=?";
        Object[] queryVals = new Object[vals.size()];
        queryVals[0] = vals.get(0);

        //if more than one columns are specified
        if(cols.size()>1){

            //for the remaining column/value pairs, finishes the SQL query and collects up values to be used
            for(int i = 1; i<cols.size(); i++){
                sql+=" AND " + cols.get(i) + "=?";
                queryVals[i] = vals.get(i);
            }
        }

        //apply ordering on results, if specified
        if(!sort.isEmpty()) sql = sql + " ORDER BY " + sort.get(0) +  " " + sort.get(1);

        //execute sql query
        List<BeerStocked> searchResults = jdbcTemplate.query(sql, queryVals, new BeerStockedRowMapper());

        return searchResults;
    }

    public int deleteById(long id) {
        return jdbcTemplate.update("delete from beerStocked where id=?", new Object[] { id });
    }

    public int insert(BeerStocked beer) {
        return jdbcTemplate.update("insert into beerStocked (id, name, country, brewer, type, abv) " + "values(?,  ?, ?)",
                new Object[] { beer.getId(), beer.getName(), beer.getCountry(), beer.getBrewer(), beer.getType(), beer.getAbv()});
    }

    public int update(BeerStocked beer) {
        return jdbcTemplate.update("update beerStocked " + " set name = ?, country = ? , brewer = ?, type = ?, abv = ?" + " where id = ?",
                new Object[] { beer.getName(), beer.getCountry(), beer.getBrewer(), beer.getType(), beer.getAbv(), beer.getId()});
    }

}
