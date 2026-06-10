import com.kms.katalon.core.annotation.BeforeTestCase
import com.kms.katalon.core.configuration.RunConfiguration
import com.kms.katalon.core.context.TestCaseContext
import com.kms.katalon.core.util.KeywordUtil
import internal.GlobalVariable as GlobalVariable

/**
 * SeedReset — Fixture di TEST ENVIRONMENT (precondizione), non logica di test.
 *
 * Prima di OGNI test case ripristina i dati seed al baseline as-delivered
 * eseguendo il client mysql sullo script "Data Files/reset-seed.sql"
 * (gli INSERT sono il seed ufficiale di db/storage.sql).
 *
 * Garantisce indipendenza e ripetibilita' dei test (niente cascata da stato
 * condiviso mutato). NON e' un oracolo: nessun test usa il DB per decidere
 * pass/fail. Gli oracoli restano osservati dalla UI (black-box).
 *
 * Config in Profiles/default.glbl: mysqlExe, dbUser, dbPass, dbName, resetSeed.
 */
class SeedReset {

	@BeforeTestCase
	def resetSeedBeforeTestCase(TestCaseContext context) {

		// Interruttore globale (default true): consente di disattivare il reset.
		if (GlobalVariable.resetSeed == false) {
			return
		}

		String projectDir = RunConfiguration.getProjectDir()
		File script = new File(projectDir, 'Data Files/reset-seed.sql')

		if (!script.exists()) {
			KeywordUtil.markFailed('Fixture reset: file non trovato -> ' + script.getAbsolutePath())
			return
		}

		// mysql <db> --user=.. --password=..  < reset-seed.sql
		List<String> cmd = [
			GlobalVariable.mysqlExe as String,
			'--user=' + GlobalVariable.dbUser,
			'--password=' + GlobalVariable.dbPass,
			GlobalVariable.dbName as String
		]

		File log = new File(System.getProperty('java.io.tmpdir'), 'seedreset.log')

		ProcessBuilder pb = new ProcessBuilder(cmd)
		pb.redirectInput(script)
		pb.redirectErrorStream(true)
		pb.redirectOutput(log)

		Process p = pb.start()
		// Timeout di sicurezza: la fixture non deve MAI appendere la suite.
		boolean finished = p.waitFor(20, java.util.concurrent.TimeUnit.SECONDS)
		if (!finished) {
			p.destroyForcibly()
			KeywordUtil.markFailed('Fixture reset seed: timeout 20s (possibile lock sul DB). Vedi ' + log.getAbsolutePath())
			return
		}

		int code = p.exitValue()
		if (code != 0) {
			// Fallimento del ripristino ambiente = precondizione non soddisfatta.
			String output = log.exists() ? log.getText('UTF-8') : ''
			KeywordUtil.markFailed('Fixture reset seed fallita (exit=' + code + '): ' + output)
		} else {
			KeywordUtil.logInfo('Seed ripristinato prima di: ' + context.getTestCaseId())
		}
	}
}
