package com.blog.utils;

public class UtilsList {
	
	private UtilsList() {}

	/**
	 * Retorna qual será a página inicial de uma consulta e a página final. Útil
	 * para paginação.
	 * 
	 * @param actualPage mínimo é 0.
	 * @param pageSize   o tamanho da página da paginação do banco
	 * @return
	 */
	public static int[] calcPagingRowsNew(int actualPage, int pageSize) {
		int initialNumber = 0;
		int finalNumber = 0;

		initialNumber = ((actualPage * pageSize) - pageSize);
		initialNumber = actualPage > 1 ? initialNumber + 1 : initialNumber;
		finalNumber = actualPage > 1 ? ((pageSize * actualPage) + 1) : pageSize;

		return new int[] { initialNumber, finalNumber };
	}
}