/*
 * Copyright contributors to Hyperledger Besu
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 */
package org.hyperledger.besu.evm.operations.linea;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.hyperledger.besu.evm.EVM;
import org.hyperledger.besu.evm.frame.BlockValues;
import org.hyperledger.besu.evm.frame.MessageFrame;
import org.hyperledger.besu.evm.gascalculator.LondonGasCalculator;
import org.hyperledger.besu.evm.operation.Operation;
import org.hyperledger.besu.evm.operation.linea.ZeroPrevRanDaoOperation;

import org.apache.tuweni.bytes.Bytes;
import org.apache.tuweni.bytes.Bytes32;
import org.apache.tuweni.units.bigints.UInt256;
import org.junit.jupiter.api.Test;

public class ZeroPrevRanDaoOperationTest {

  @Test
  public void pushesZeroWhenDifficultyZero() {
    ZeroPrevRanDaoOperation op = new ZeroPrevRanDaoOperation(new LondonGasCalculator());
    MessageFrame messageFrame = mock(MessageFrame.class);
    BlockValues blockHeader = mock(BlockValues.class);
    Bytes32 prevRandao = Bytes32.fromHexString("0xb0b0face");
    when(blockHeader.getDifficultyBytes()).thenReturn(UInt256.ZERO);
    when(blockHeader.getMixHashOrPrevRandao()).thenReturn(prevRandao);
    when(messageFrame.getBlockValues()).thenReturn(blockHeader);
    EVM evm = mock(EVM.class);
    Operation.OperationResult r = op.executeFixedCostOperation(messageFrame, evm);
    assertThat(r.getHaltReason()).isNull();
    verify(messageFrame).pushStackItem(UInt256.ZERO);
  }

  @Test
  public void pushesZeroWhenDifficultyPresent() {
    ZeroPrevRanDaoOperation op = new ZeroPrevRanDaoOperation(new LondonGasCalculator());
    MessageFrame messageFrame = mock(MessageFrame.class);
    BlockValues blockHeader = mock(BlockValues.class);
    Bytes32 prevRandao = Bytes32.fromHexString("0xb0b0face");
    Bytes difficulty = Bytes.random(32);
    when(blockHeader.getDifficultyBytes()).thenReturn(difficulty);
    when(blockHeader.getMixHashOrPrevRandao()).thenReturn(prevRandao);
    when(messageFrame.getBlockValues()).thenReturn(blockHeader);
    EVM evm = mock(EVM.class);
    Operation.OperationResult r = op.executeFixedCostOperation(messageFrame, evm);
    assertThat(r.getHaltReason()).isNull();
    verify(messageFrame).pushStackItem(UInt256.ZERO);
  }
}
