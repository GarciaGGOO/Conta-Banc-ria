import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.junit.Assert;

/**
 * Classe de testes para verificar o comportamento de saques em contas especiais e normais.
 */
public class Conta {

    private boolean clienteEspecial;
    private int saldoConta;
    private boolean saqueEfetuado;
    private String mensagemErro;

    /**
     * Configura um cliente com saldo inicial.
     *
     * @param tipo   O tipo de cliente (especial ou normal).
     * @param saldo  O saldo inicial da conta.
     */
    @Given("^um cliente (especial|normal) com saldo atual de -(\\d+) reais$")
    public void givenUmClienteComSaldoAtualDeReais(String tipo, int saldo) {
        clienteEspecial = "especial".equalsIgnoreCase(tipo);
        saldoConta = saldo;
        saqueEfetuado = false;
        mensagemErro = null;
    }

    /**
     * Simula a solicitação de um saque.
     *
     * @param saque O valor do saque solicitado.
     */
    @When("^for solicitado um saque no valor de (\\d+) reais$")
    public void whenForSolicitadoUmSaqueNoValorDeReais(int saque) {
        if (podeRealizarSaque(saque)) {
            saldoConta -= saque;
            saqueEfetuado = true;
        } else {
            saqueEfetuado = false;
            mensagemErro = "Saldo insuficiente para realizar o saque";
        }
    }

    /**
     * Verifica se o saque foi efetuado com sucesso e se o saldo foi atualizado corretamente.
     *
     * @param novoSaldo O novo saldo esperado após o saque.
     */
    @Then("^deve efetuar o saque e atualizar o saldo da conta para -(\\d+) reais$")
    public void thenDeveEfetuarOSaqueEAtualizarOSaldoDaContaParaReais(int novoSaldo) {
        if (saqueEfetuado) {
            Assert.assertEquals(novoSaldo, saldoConta);
        } else {
            Assert.fail("O saque não foi efetuado corretamente.");
        }
    }

    /**
     * Verifica se o saque não foi efetuado para clientes normais e se a mensagem de erro é a esperada.
     */
    @Then("^não deve efetuar o saque e deve retornar a mensagem Saldo Insuficiente$")
    public void thenNaoDeveEfetuarOSaque() {
        if (!saqueEfetuado) {
            Assert.assertEquals("Saldo insuficiente para realizar o saque", mensagemErro);
        } else {
            Assert.fail("O saque deveria ter falhado, mas foi efetuado.");
        }
    }

    /**
     * Verifica se é possível realizar o saque com base no tipo de cliente.
     *
     * @param saque O valor do saque solicitado.
     * @return true se o saque pode ser realizado, false caso contrário.
     */
    private boolean podeRealizarSaque(int saque) {
        if (clienteEspecial) {
            return saldoConta >= saque;
        } else {
            return false;  // Clientes normais não podem efetuar o saque.
        }
    }
}
