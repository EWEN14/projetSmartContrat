import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

public class RuralBookContractDeployment {

    private static final String RPC_SERVER_URL = "http://localhost:7545"; // URL du serveur RPC Ganache
    private static final String PRIVATE_KEY = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"; // Clé privée du compte déployant le contrat
    private static final String CONTRACT_OWNER_ADDRESS = "0x1234567890123456789012345678901234567890"; // Adresse du propriétaire du contrat

    public static void main(String[] args) throws Exception {
        // Connexion au serveur RPC Ganache
        Web3j web3j = Web3j.build(new HttpService(RPC_SERVER_URL));

        // Chargement des informations d'identification du compte déployant le contrat
        Credentials credentials = Credentials.create(PRIVATE_KEY);

        // Création de l'objet de déploiement du contrat
        RuralBook ruralBook = RuralBook.deploy(
            web3j,
            credentials,
            ManagedTransaction.GAS_PRICE,
            Contract.GAS_LIMIT,
            new Utf8String("RuralBook"),
            new Utf8String("1.0"),
            Arrays.asList(),
            Arrays.asList(),
            new Utf8String("LandInformation"),
            Arrays.asList(
                new Utf8String("owner"),
                new Uint256(BigInteger.ZERO),
                new Utf8String("gpsLocations")
            )
        ).send();

        // Enregistrement du contrat déployé dans la blockchain
        TransactionReceipt transactionReceipt = ruralBook
            .addLand(
                new Address(CONTRACT_OWNER_ADDRESS),
                new Utf8String("John Doe"),
                new Uint256(100),
                Collections.singletonList(new Utf8String("40.7128° N, 74.0060° W"))
            )
            .send();

        // Affichage des informations du contrat déployé
        System.out.println("Contract deployed at: " + ruralBook.getContractAddress());
        System.out.println("Transaction hash: " + transactionReceipt.getTransactionHash());
    }
}
