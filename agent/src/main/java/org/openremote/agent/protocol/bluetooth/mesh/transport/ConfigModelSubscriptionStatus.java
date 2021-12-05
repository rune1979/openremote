package org.openremote.agent.protocol.bluetooth.mesh.transport;

import org.openremote.agent.protocol.bluetooth.mesh.opcodes.ConfigMessageOpCodes;
import org.openremote.agent.protocol.bluetooth.mesh.utils.MeshAddress;
import org.openremote.agent.protocol.bluetooth.mesh.utils.MeshParserUtils;

import java.util.logging.Logger;

/**
 * Creates the ConfigModelSubscriptionStatus Message.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class ConfigModelSubscriptionStatus extends ConfigStatusMessage {

    public static final Logger LOG = Logger.getLogger(ConfigModelSubscriptionStatus.class.getName());
    private static final int OP_CODE = ConfigMessageOpCodes.CONFIG_MODEL_SUBSCRIPTION_STATUS;
    private static final int CONFIG_MODEL_PUBLICATION_STATUS_SIG_MODEL_PDU_LENGTH = 7;
    private static final int CONFIG_MODEL_APP_BIND_STATUS_VENDOR_MODEL_PDU_LENGTH = 9;
    private int mElementAddress;
    private int mModelIdentifier;
    private int mSubscriptionAddress;

    /**
     * Constructs the ConfigModelSubscriptionStatus mMessage.
     *
     * @param message Access Message
     */
    public ConfigModelSubscriptionStatus(final AccessMessage message) {
        super(message);
        this.mParameters = message.getParameters();
        parseStatusParameters();
    }

    @Override
    final void parseStatusParameters() {
        final AccessMessage message = (AccessMessage) mMessage;
        mStatusCode = mParameters[0];
        mStatusCodeName = getStatusCodeName(mStatusCode);
        mElementAddress = MeshParserUtils.unsignedBytesToInt(mParameters[1], mParameters[2]);

        mSubscriptionAddress = MeshParserUtils.unsignedBytesToInt(mParameters[3], mParameters[4]);

        final byte[] modelIdentifier;
        if (mParameters.length == CONFIG_MODEL_PUBLICATION_STATUS_SIG_MODEL_PDU_LENGTH) {
            mModelIdentifier = MeshParserUtils.unsignedBytesToInt(mParameters[5], mParameters[6]);
        } else {
            //modelIdentifier = new byte[]{mParameters[6], mParameters[5], mParameters[8], mParameters[7]};
            mModelIdentifier = MeshParserUtils.bytesToInt(new byte[]{mParameters[6], mParameters[5], mParameters[8], mParameters[7]});
        }

        LOG.info("Status code: " + mStatusCode);
        LOG.info("Status message: " + mStatusCodeName);
        LOG.info("Element Address: " + MeshAddress.formatAddress(mElementAddress, true));
        LOG.info("Subscription Address: " + MeshAddress.formatAddress(mSubscriptionAddress, true));
        LOG.info("Model Identifier: " + Integer.toHexString(mModelIdentifier));
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
     * Returns the subscription address.
     *
     * @return subscription address
     */
    public int getSubscriptionAddress() {
        return mSubscriptionAddress;
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
