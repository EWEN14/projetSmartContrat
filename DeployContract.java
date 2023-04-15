import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Contract;
import org.web3j.tx.ManagedTransaction;

public class BailRuralContractDeployment {

    private static final String RPC_SERVER_URL = "http://localhost:7545"; // URL du serveur RPC Ganache
    private static final String PRIVATE_KEY = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"; // Clé privée du compte déployant le contrat
    private static final String CONTRACT_OWNER_ADDRESS = "0x1234567890123456789012345678901234567890"; // Adresse du propriétaire du contrat

    public static void main(String[] args) throws Exception {
        // Connexion au serveur RPC Ganache
        Web3j web3j = Web3j.build(new HttpService(RPC_SERVER_URL));

        // Chargement des informations d'identification du compte déployant le contrat
        Credentials credentials = Credentials.create(PRIVATE_KEY);

        // Création de l'objet de déploiement du contrat
        BailRural contract = BailRural.deploy(
            web3j,
            credentials,
            ManagedTransaction.GAS_PRICE,
            Contract.GAS_LIMIT
        ).send();

        // Enregistrement du contrat déployé dans la blockchain
        TransactionReceipt transactionReceipt = contract.deploy().send();

        // Affichage de l'adresse du contrat déployé
        System.out.println("Contract deployed at: " + contract.getContractAddress());

        // Enregistrement des informations de propriété foncière pour l'adresse du propriétaire du contrat
        String owner = "John Doe";
        List<Uint256> gpsPoints = Arrays.asList(
            new Uint256(BigInteger.valueOf(16621704)),
            new Uint256(BigInteger.valueOf(-21615572)),
            new Uint256(BigInteger.valueOf(166219218)),
            new Uint256(BigInteger.valueOf(-21616968)),
            new Uint256(BigInteger.valueOf(166217426)),
            new Uint256(BigInteger.valueOf(-21618046)),
            new Uint256(BigInteger.valueOf(166215205)),
            new Uint256(BigInteger.valueOf(-21617367)),
            new Uint256(BigInteger.valueOf(16621704)),
            new Uint256(BigInteger.valueOf(-21615572))
        );
        contract.recordLand(
            new Utf8String(owner),
            gpsPoints
        ).send();
        
        // Affichage des informations de propriété foncière enregistrées
        String address = CONTRACT_OWNER_ADDRESS;
        List<Type> result = contract.getLandInformation(
            new Address(address)
        ).send();
        String landOwner = (String) result.get(0).getValue();
        List<Uint256> landGpsPoints = (List<Uint256>) result.get(1).getValue();
        System.out.println("Land information for address " + address + ":");
        System.out.println("Owner: "
