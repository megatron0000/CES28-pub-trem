package Q2;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import Q2.ControladorPTC;
import Q2.Datacenter;
import Q2.PainelCondutor;
import Q2.Sensor;

/**
 * Alguns detalhes não são explicados no roteiro do laboratório sobre o cenário
 * descrito, então assumo alguns pontos por completude: 
 *   - Prioridade=1 significa mensagem normal 
 *   - Prioridade>1 é mensagem urgente 
 *   - Mensagens são sempre enviadas para o Datacenter, esteja o trem em situação de prioridade ou não
 * 
 * @author Vitor
 *
 */
public class ControladorPTCTeste {

	private Sensor sensor;
	private Datacenter datacenter;
	private PainelCondutor painelCond;
	private ControladorPTC controlador;

	@Before
	public void setUp() {
		sensor = mock(Sensor.class);
		datacenter = mock(Datacenter.class);
		painelCond = mock(PainelCondutor.class);
		controlador = new ControladorPTC(sensor, datacenter, painelCond);
	}

	/**
	 * Teste da letra A. Como os campo são privados em ControladorPTC, não vejo
	 * como posso testar se eles foram inicializados com os mocks
	 */
	@Test
	public void testaConstrutor() {
		@SuppressWarnings("unused")
		ControladorPTC contPTC = new ControladorPTC(sensor, datacenter, painelCond);
		verifyZeroInteractions(sensor, datacenter, painelCond);
	}

	/**
	 * Teste da letra B. Ele passa em verde.
	 * 
	 * Configuro o mock para dizer que não está em cruzamento e que a velocidade
	 * é 50
	 */
	@Test
	public void testaNaoEstaEmCruzamento() {
		when(sensor.isCruzamento()).thenReturn(false);
		when(sensor.getVelocidade()).thenReturn(50);

		assertTrue(controlador.enviaMsgPrioritariaPainel("qualquer mensagem", painelCond));
		controlador.run();

		verify(painelCond).imprimirAviso(eq("50.0"), anyInt());
		verify(datacenter).gerarRelatorio();
	}

	/**
	 * Teste da letra C. Ele falha.
	 */
	@Test
	public void testaCruzamentoComVelocidadeExcessiva() {
		when(sensor.isCruzamento()).thenReturn(true);
		when(sensor.getVelocidade()).thenReturn(5000); // E quanto excesso !

		// painel responde ao aviso emergencial
		when(painelCond.imprimirAviso(anyString(), anyInt())).thenReturn(true);

		// controlador reconhece que painel respondeu ao aviso emergencial
		assertTrue(controlador.enviaMsgPrioritariaPainel("qualquer mensagem", painelCond));

		controlador.run();

		// como o painel respondeu, o controlador não deve forçar velocidade
		// nenhuma
		verify(painelCond, never()).diminuiVelocidadeTrem(anyInt());

		// a mensagem enviada ao painel deve ter sido urgente (prioridade>1
		// pelas minhas hipóteses)
		ArgumentCaptor<Integer> prioridade = ArgumentCaptor.forClass(Integer.class);
		verify(painelCond).imprimirAviso(eq("Velocidade alta"), prioridade.capture());
		assertTrue(prioridade.getValue() > 1);

		// mesmo em situação de emergência, o datacenter é sempre mantido
		// informado
		verify(datacenter).gerarRelatorio();

	}

	/**
	 * Teste da letra D. Ele falha
	 */
	public void testaCruzamentoComVelocidadePequenaDemais() {
		when(sensor.isCruzamento()).thenReturn(true);
		when(sensor.getVelocidade()).thenReturn(1); // devagar, quase parando...

		// painel não responde ao aviso emergencial
		when(painelCond.imprimirAviso(anyString(), anyInt())).thenReturn(false);

		// controlador reconhece que painel não respondeu
		assertFalse(controlador.enviaMsgPrioritariaPainel("qualquer mensagem", painelCond));

		controlador.run();

		// o painel não respondeu, então o controlador tem que forçar aumento de
		// velocidade
		verify(painelCond).aceleraVelocidadeTrem(20);

		// a mensagem enviada ao painel deve ter sido urgente (prioridade>1
		// pelas minhas hipóteses)
		ArgumentCaptor<Integer> prioridade = ArgumentCaptor.forClass(Integer.class);
		verify(painelCond).imprimirAviso(eq("Velocidade Baixa"), prioridade.capture());
		assertTrue(prioridade.getValue() > 1);

		// mesmo em situação de emergência, o datacenter é sempre mantido
		// informado
		verify(datacenter).gerarRelatorio();

	}

}
