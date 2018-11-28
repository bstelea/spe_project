package com.example.globalbeershop.BeerStocked;

import java.sql.ResultSet;
        import java.sql.SQLException;
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

    public List<BeerStocked> findAll() {
        return jdbcTemplate.query("select * from beerStocked", new BeerStockedRowMapper());
    }

    public BeerStocked findById(long id) {
        return jdbcTemplate.queryForObject("select * from beerStocked where id=?", new Object[] { id },
                new BeerStockedRowMapper());
    }

    //searching by column(s)
    public List<BeerStocked> findByColumn(List<String> cols, List<Object> vals){

        //if no cols/vals given, or diff numbers of them are given; return all results
        if(cols.size() == 0 || vals.size() == 0 || vals.size() != cols.size()){
            return findAll();
        }

        //the basic sql statement for at least 1 column
        String sql = "select * from beerStocked where " + cols.get(0) + "=?";

        //if only one column is specified
        if(cols.size()==1){
            return jdbcTemplate.query(sql, new Object[]{vals.get(0)}, new BeerStockedRowMapper());
        }

        //if multiple columns are specified
        else{

            Object[] queryVals = new Object[vals.size()];
            queryVals[0] = vals.get(0);

            //for the remaining column/value pairs, finishes the SQL query and collects up values to be used
            for(int i = 1; i<cols.size(); i++){
                sql+=" AND " + cols.get(i) + " = ?";
                queryVals[i] = vals.get(i);
            }

            return jdbcTemplate.query(sql, queryVals, new BeerStockedRowMapper());
        }
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
