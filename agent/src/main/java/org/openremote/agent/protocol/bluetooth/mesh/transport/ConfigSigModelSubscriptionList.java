package org.openremote.agent.protocol.bluetooth.mesh.transport;

import org.openremote.agent.protocol.bluetooth.mesh.opcodes.ConfigMessageOpCodes;
import org.openremote.agent.protocol.bluetooth.mesh.utils.MeshAddress;
import org.openremote.agent.protocol.bluetooth.mesh.utils.MeshParserUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Creates the ConfigModelSubscriptionStatus Message.
 * <p> This message lists all subscription addresses for a SIG Models </p>
 */
public class ConfigSigModelSubscriptionList extends ConfigStatusMessage {

    public static final Logger LOG = Logger.getLogger(ConfigSigModelSubscriptionList.class.getName());
    private static final int OP_CODE = ConfigMessageOpCodes.CONFIG_SIG_MODEL_SUBSCRIPTION_LIST;
    private int mElementAddress;
    private int mModelIdentifier;
    private final List<Integer> mSubscriptionAddresses;

    /**
     * Constructs the ConfigModelSubscriptionStatus mMessage.
     *
     * @param message Access Message
     */
    public ConfigSigModelSubscriptionList(final AccessMessage message) {
        super(message);
        mSubscriptionAddresses = new ArrayList<>();
        this.mParameters = message.getParameters();
        parseStatusParameters();
    }

    @Override
    final void parseStatusParameters() {
        final AccessMessage message = (AccessMessage) mMessage;
        mStatusCode = mParameters[0];
        mStatusCodeName = getStatusCodeName(mStatusCode);
        mElementAddress = MeshParserUtils.unsignedBytesToInt(mParameters[1], mParameters[2]);
        mModelIdentifier = MeshParserUtils.unsignedBytesToInt(mParameters[3], mParameters[4]);

        LOG.info("Status code: " + mStatusCode);
        LOG.info("Status message: " + mStatusCodeName);
        LOG.info("Element Address: " + MeshAddress.formatAddress(mElementAddress, true));
        LOG.info("Model Identifier: " + Integer.toHexString(mModelIdentifier));

        for (int i = 5; i < mParameters.length; i += 2) {
            final int address = MeshParserUtils.unsignedBytesToInt(mParameters[i], mParameters[i + 1]);
            mSubscriptionAddresses.add(address);
            LOG.info("Subscription Address: " + MeshAddress.formatAddress(address, false));
        }
    }

    @Override
    public int getOpCode() {
        return OP_CODE;
    }

    /**
     * Returns the element address that the key was bound to
     *
     * @return element address
     */
    public int getElementAddress() {
        return mElementAddress;
    }

    /**
     * Returns the list of subscription addresses.
     *
     * @return subscription address
     */
    public List<Integer> getSubscriptionAddresses() {
        return mSubscriptionAddresses;
    }

    /**
     * Returns the model identifier
     *
     * @return 16-bit sig model identifier or 32-bit vendor model identifier
     */
    public final int getModelIdentifier() {
        return mModelIdentifier;
    }

    /**
     * Returns if the message was successful
     *
     * @return true if the message was successful or false otherwise
     */
    public final boolean isSuccessful() {
        return mStatusCode == 0x00;
    }
}
