package com.fusion.bank.wallet.infra.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fusion.bank.wallet.shared.exception.DeserializerFailed;
import java.io.IOException;
import java.util.UUID;

public class DeserializerUUID extends JsonDeserializer<UUID> {

    @Override
    public UUID deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        String PureId = jsonParser.getText();
        if (PureId == null || PureId.isEmpty()) {
            throw new DeserializerFailed();
        }

        if (!PureId.contains("-") && PureId.length() == 32) {

            String formatId = PureId.replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
            return UUID.fromString(formatId.toLowerCase());
        }

        return UUID.fromString(PureId.toLowerCase());

    }
}
