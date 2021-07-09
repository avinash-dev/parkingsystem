package com.parkit.parkingsystem.UnitTests;

import com.parkit.parkingsystem.util.InputReaderUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Scanner;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class InputReaderUtilTest {

	@Mock
	private static final Scanner scanner = new Scanner(System.in);
	private static InputReaderUtil inputReaderUtilUnderTest;

	@BeforeEach
	private void setScanner() {
		inputReaderUtilUnderTest = new InputReaderUtil(scanner);

	}

	@Disabled
	@Test
	public void InputReaderReturnValue() {
		// GIVEN
		when(scanner.nextLine()).thenReturn("1");

		// WHEN
		int result = inputReaderUtilUnderTest.readSelection();

		// THEN
		verify(scanner, times(1)).nextLine();
		assertThat(result).isEqualTo(1);
	}
}
