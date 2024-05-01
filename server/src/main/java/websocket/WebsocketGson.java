package websocket;

import chess.ChessMove;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class WebsocketGson {
    public static GsonBuilder getBuilder() {
        return new GsonBuilder().registerTypeAdapter(ChessMove.class, new TypeAdapter<ChessMove>() {
            @Override
            public void write(JsonWriter jsonWriter, ChessMove move) throws IOException {
                if(move != null) jsonWriter.value(move.toString());
                else jsonWriter.nullValue();
            }

            @Override
            public ChessMove read(JsonReader jsonReader) throws IOException {
                return new ChessMove(jsonReader.nextString());
            }
        });
    }
}
