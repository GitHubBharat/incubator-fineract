package org.mifosplatform.portfolio.client.serialization;

import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.mifosplatform.infrastructure.core.serialization.AbstractFromApiJsonDeserializer;
import org.mifosplatform.infrastructure.core.serialization.CommandSerializer;
import org.mifosplatform.infrastructure.core.serialization.FromApiJsonDeserializer;
import org.mifosplatform.infrastructure.core.serialization.FromJsonHelper;
import org.mifosplatform.portfolio.client.command.ClientCommand;
import org.mifosplatform.portfolio.client.command.ClientIdentifierCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.reflect.TypeToken;

/**
 * Implementation of {@link FromApiJsonDeserializer} for {@link ClientCommand} 
 * 's.
 */
@Component
public final class ClientIdentifierCommandFromApiJsonDeserializer extends AbstractFromApiJsonDeserializer<ClientIdentifierCommand> {

    /**
     * The parameters supported for this command.
     */
    private final Set<String> supportedParameters = new HashSet<String>(Arrays.asList("clientId", "documentTypeId", "documentKey",
            "description"));

    private final FromJsonHelper fromApiJsonHelper;
    private final ClientIdentifierCommandFromCommandJsonDeserializer commandFromCommandJsonDeserializer;
    private final CommandSerializer commandSerializerService;

    @Autowired
    public ClientIdentifierCommandFromApiJsonDeserializer(final FromJsonHelper fromApiJsonHelper,
            final ClientIdentifierCommandFromCommandJsonDeserializer commandFromCommandJsonDeserializer,
            final CommandSerializer commandSerializerService) {
        super(commandSerializerService);
        this.fromApiJsonHelper = fromApiJsonHelper;
        this.commandFromCommandJsonDeserializer = commandFromCommandJsonDeserializer;
        this.commandSerializerService = commandSerializerService;
    }

    @Override
    public ClientIdentifierCommand commandFromApiJson(final Long clientIdentifierId, final String json) {

        final Type typeOfMap = new TypeToken<Map<String, Object>>() {}.getType();

        fromApiJsonHelper.checkForUnsupportedParameters(typeOfMap, json, supportedParameters);

        return this.commandFromCommandJsonDeserializer.commandFromCommandJson(clientIdentifierId, json);
    }

    public String serializedCommandJsonFromApiJsonWithSubResource(final String json, final Long clientId) {
       return serializedCommandJsonFromApiJsonWithSubResource(null, json, clientId);
    }
    
    public String serializedCommandJsonFromApiJsonWithSubResource(final Long clientIdentifierId, final String json, final Long clientId) {
        final ClientIdentifierCommand command = commandFromApiJson(clientIdentifierId, json);
        final ClientIdentifierCommand commandWithSubResource = ClientIdentifierCommand.clientSubResource(command, clientId);
        return this.commandSerializerService.serializeCommandToJson(commandWithSubResource);
    }
}