package DB;

import javafx.util.Pair;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.StringJoiner;

final class SQLGenerator {
    private  SQLGenerator(){}

    static String Insert(String table, List<Pair<String, String>> data){

        StringJoiner keyJoiner = new StringJoiner(", ");
        StringJoiner valJoiner = new StringJoiner(", ");
        for (Pair<String, String> p:data)
        {
          keyJoiner.add("'" + p.getKey() + "'");
          valJoiner.add("'" + p.getValue() + "'");
        }

        StringBuilder builder = new StringBuilder();
        builder.append("INSERT INTO ")
                .append(table).append(" ( ")
                .append(keyJoiner.toString())
                .append(" ) VALUES ( ")
                .append(valJoiner.toString())
                .append(" );");
        return builder.toString();
    }
}
