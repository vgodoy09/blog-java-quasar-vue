package com.blog.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UtilsListTest {

	@Test
	void testSuccessCalcPagingRowsNew() {
		int[] calcPagingRowsNew = UtilsList.calcPagingRowsNew(1, 10);
		assertThat(calcPagingRowsNew[0], is(0));
		assertThat(calcPagingRowsNew[1], is(10));
	}
	
	@Test
	void testSuccessCalcPagingRowsNewActualPageMaiorQueUm() {
		int[] calcPagingRowsNew = UtilsList.calcPagingRowsNew(2, 10);
		assertThat(calcPagingRowsNew[0], is(11));
		assertThat(calcPagingRowsNew[1], is(21));
	}
	
	@Test
	void testSuccessCalcPagingRowsNewActualPageMaiorTres() {
		int[] calcPagingRowsNew = UtilsList.calcPagingRowsNew(3, 10);
		assertThat(calcPagingRowsNew[0], is(21));
		assertThat(calcPagingRowsNew[1], is(31));
	}
	
	
}
