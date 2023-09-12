package com.alura.jdbc.dao;import com.alura.jdbc.factory.ConnectionFactory;import com.alura.jdbc.modelo.Producto;import java.sql.*;import java.util.ArrayList;import java.util.List;public class ProductoDAO {    //Atributo de la conexion    final private Connection con;    //Se asigna la conexion en un contructor    public ProductoDAO(Connection con){        //Asignamos la variable con al constructor connection que tenemos en la clase        this.con = con;    }    //Acá se guardara toda la logica del productoController    public void guardar(Producto producto) {        try(con){            final PreparedStatement statement = con.prepareStatement(                         "INSERT INTO PRODUCTO "                            + "(nombre, descripcion, cantidad)"                            + " VALUES(?,?,?)",                    Statement.RETURN_GENERATED_KEYS);            try(statement) {                EjecutaReguistro(producto, statement);                }            }catch (SQLException e){            throw new RuntimeException(e);        }    }    private static void EjecutaReguistro(Producto producto,PreparedStatement statement) throws SQLException {        //Setteando los valores de la query, estos tienen que ir en el mismo orden en el que lo pusimos en el query        statement.setString(1, producto.getNombre());        statement.setString(2, producto.getDescripcion());        statement.setInt(3, producto.getCantidad());        statement.execute();        //---------Forma de java 9 en adelante-----------        final ResultSet resultSet = statement.getGeneratedKeys();        try(resultSet)/*En esta variable tenemos el listado de IDs que fueron generados)*/{            //Con este loop, podemos listar el listado de IDs que fue generado            while (resultSet.next()) {                producto.setId(resultSet.getInt(1));                System.out.println(String.format("Fue insertado el producto %s", producto));            }        }    }    public static List<Producto> listar(){        List<Producto>  resultado = new ArrayList<>();        final Connection con = new ConnectionFactory().recuperaConexion();        try(con) {            //-------PASANDO LA RERPONSABILDIAD DE VER LOS VALORES AL JDBC-----------            final PreparedStatement statement = con.prepareStatement("SELECT ID, NOMBRE, DESCRIPCION, CANTIDAD FROM PRODUCTO");            try(statement) {                statement.execute();                //tomando el resultado del statement/query select                final ResultSet resultSet = statement.getResultSet();                //Para ir ingresando un valor abajo de un valor, hacemos un recorrido de toda la tabla, valor por valor                try(resultSet) {                    while (resultSet.next()){                        Producto fila = new Producto(resultSet.getInt("ID"),                                resultSet.getString("NOMBRE"),                                resultSet.getString("DESCRIPCION"),                                resultSet.getInt("CANTIDAD"));                                resultado.add(fila);//cada resultado lo vamos agregando a las filas                    }                }            }            return resultado;        }catch (SQLException e){            throw new RuntimeException(e);            }    }    public int eliminar(Integer id)  {        try(con){            //-------PASANDO LA RERPONSABILDIAD DE VER LOS VALORES AL JDBC-----------            final PreparedStatement statement = con.prepareStatement("DELETE FROM PRODUCTO WHERE ID = ?");            try(statement) {                statement.setInt(1, id);                statement.execute();                //Para contar cuntas filas fueron modificadas                int updateCount = statement.getUpdateCount();                return updateCount;            }        }catch (SQLException e){                throw new RuntimeException(e);            }    }    public int modificar(String nombre, String descripcion, Integer cantidad, Integer id) {        try{            //-------PASANDO LA RERPONSABILDIAD DE VER LOS VALORES AL JDBC-----------            final PreparedStatement statement = con.prepareStatement("UPDATE PRODUCTO SET "                    + " NOMBRE = ?"                    + ", DESCRIPCION = ?"                    + ", CANTIDAD = ?"                    + " WHERE ID = ?" );            try(statement) {                statement.setString(1,nombre);                statement.setString(2,descripcion);                statement.setInt(3,cantidad);                statement.setInt(4,id);                statement.execute();                //Para contar cuntas filas fueron modificadas                int updateCount = statement.getUpdateCount();                return updateCount;            }        }catch (SQLException e){            throw new RuntimeException(e);        }    }}