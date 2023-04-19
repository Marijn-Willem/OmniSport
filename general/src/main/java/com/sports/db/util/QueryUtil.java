package com.sports.db.util;

import com.microsoft.sqlserver.jdbc.Geometry;
import com.microsoft.sqlserver.jdbc.SQLServerResultSet;
import com.sports.db.type.Point;
import com.sports.logic.util.Util;
import org.postgresql.geometric.PGpoint;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class QueryUtil {
    private static DatabaseType dbType = DatabaseType.POSTGRES;

    public static void setDbType(String dbType) {
        if (dbType.equals("postgres"))
            QueryUtil.dbType = DatabaseType.POSTGRES;
        else if (dbType.equals("sqlserver"))
            QueryUtil.dbType = DatabaseType.SQLSERVER;
    }

    public static String getDriverName() {
        if (dbType == DatabaseType.SQLSERVER)
            return "com.microsoft.sqlserver.jdbc.SQLServerDriver";

        return "org.postgresql.Driver";
    }

    public static String getJdbcPrefix() {
        if (dbType == DatabaseType.SQLSERVER)
            return "jdbc:sqlserver";

        return "jdbc:postgresql";
    }

    public static String getDbCurrentTime() {
        if (dbType == DatabaseType.SQLSERVER)
            return "GETDATE()";

        return "NOW()";
    }

    public static String convertStringToDbValue(String str) {
        return str == null ? "NULL" : (dbType == DatabaseType.SQLSERVER ? "N" : "") +
                "'" + replaceInvalidDbCharacters(str) + "'";
    }

    public static String convertIntegerToDbValue(Integer igr) {
        if (dbType == DatabaseType.SQLSERVER)
            return igr == null ? "NULL" : igr.toString();

        return igr == null ? "NULL::integer" : igr.toString();
    }

    public static String convertBooleanToDbValue(boolean b) {
        if (dbType == DatabaseType.SQLSERVER)
            return b ? "1" : "0";

        return b ? "TRUE" : "FALSE";
    }

    public static String convertDateTimeToDbString(LocalDateTime dtTm) {
        String addition;

        if (dbType == DatabaseType.SQLSERVER)
            addition = "";
        else
            addition = "::timestamp";

        return (dtTm == null ? "NULL" : "'" + convertDateTimeToDbStringNonNull(dtTm) + "'") + addition;
    }

    public static String convertPointToDbValue(Point point) {
        if (dbType == DatabaseType.SQLSERVER) {
            return point == null ? "NULL" :
                    "GEOMETRY::Parse('POINT(" + point.x() + " " + point.y() + " NULL NULL)')";
        }
        return point == null ? "NULL::point" : "POINT(" + point.x() + ", " + point.y() + ")";
    }

    public static Integer getIntegerFromResultSet(ResultSet rs, String colName) throws SQLException {
        int colValue = rs.getInt(colName);

        return !rs.wasNull() ? colValue : null;
    }

    public static LocalDateTime convertTimestampToDateTime(Timestamp timestamp) {
        return timestamp != null ? timestamp.toLocalDateTime() : null;
    }

    public static Point getPointFromResultSet(ResultSet rs, String colName) throws SQLException {
        if (dbType == DatabaseType.SQLSERVER) {
            Geometry geometry = ((SQLServerResultSet) rs).getGeometry(colName);

            return geometry != null ? new Point(geometry.getX(), geometry.getY()) : null;
        }
        PGpoint pGpoint = (PGpoint) rs.getObject(colName);

        return pGpoint != null ? new Point(pGpoint.x, pGpoint.y) : null;
    }

    private static String replaceInvalidDbCharacters(String str) {
        return str.replace("'", "''");
    }

    private static String convertDateTimeToDbStringNonNull(LocalDateTime dtTm) {
        return dtTm.getYear() + "-" +
                Util.padCharacter(String.valueOf(dtTm.getMonthValue()), '0', 2) + "-" +
                Util.padCharacter(String.valueOf(dtTm.getDayOfMonth()), '0', 2) + " " +
                Util.padCharacter(String.valueOf(dtTm.getHour()), '0', 2) + ":" +
                Util.padCharacter(String.valueOf(dtTm.getMinute()), '0', 2) + ":" +
                Util.padCharacter(String.valueOf(dtTm.getSecond()), '0', 2);
    }

    private enum DatabaseType {
        POSTGRES,
        SQLSERVER
    }
}
