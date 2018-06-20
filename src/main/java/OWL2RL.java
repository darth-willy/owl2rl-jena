import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.jena.rdf.model.InfModel;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.reasoner.rulesys.GenericRuleReasoner;
import org.apache.jena.reasoner.rulesys.Rule;

public class OWL2RL {

	public static void main(String[] args) throws Exception {
		ClassLoader clsLoader = OWL2RL.class.getClassLoader();

		Model model = ModelFactory.createDefaultModel();
		model.read(clsLoader.getResourceAsStream("axioms.nt"), "", "N-TRIPLE");
		model.read(clsLoader.getResourceAsStream("pizza.owl"), "", "N3");

		Path path = Paths.get(clsLoader.getResource("owl2rl.jena").toURI());
		String owl2rl = new String(Files.readAllBytes(path));
		List<Rule> rules = Rule.parseRules(owl2rl);

		GenericRuleReasoner reasoner = new GenericRuleReasoner(rules);
		InfModel infModel = ModelFactory.createInfModel(reasoner, model);

		Model deductions = infModel.getDeductionsModel();
		deductions.write(System.out, "N3");
	}
}
