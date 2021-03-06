/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2020 Mohammad Altaleb
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.mohammadaltaleb.bankersalgorithm;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;

class AlgorithmRunner {
    private int processCount;
    private int resourceCount;
    private int[][] allocation;
    private int[][] need;
    private int[] available;
    private int[] processesExecutionOrder;

    AlgorithmRunner(String matricesFileName) {
        try {
            this.readMatrices(matricesFileName);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    void start() {
        if (this.isSafeState()) {
            System.out.println("Already in safe state! Processes execution order:");
            MatrixUtils.print1D(this.processesExecutionOrder);
        } else {
            System.out.println("Deadlock detected! Trying to resolve...");
            if (this.tryResolve()) {
                System.out.println("Resolved :D. Processes execution order:");
                MatrixUtils.print1D(this.processesExecutionOrder);
            } else {
                System.out.println("Could not resolve deadlock!");
            }
        }
    }

    private boolean isSafeState() {
        int[] work = available.clone();
        boolean[] finish = new boolean[processCount];
        processesExecutionOrder = new int[processCount];

        int[] zeroArray = new int[resourceCount];
        int j = 0;
        for (int i = 0; i < processCount; i++) {
            if (MatrixUtils.lessThanOrEquals(need[i], zeroArray) &&
                MatrixUtils.lessThanOrEquals(allocation[i], zeroArray)) {
                finish[i] = true;
                continue;
            }
            if (!finish[i] && MatrixUtils.lessThanOrEquals(need[i], work)) {
                work = MatrixUtils.add1D(work, allocation[i]);
                finish[i] = true;
                processesExecutionOrder[j++] = i;
                i = -1;
            }
        }

        boolean safe = true;
        for (int i = 0; i < processCount; i++) {
            safe = safe && finish[i];
        }
        return safe;
    }

    private boolean tryResolve() {
        for (int i = 0; i < processCount; i++) {
            System.out.println("Releasing Process " + i + " ...");
            int[] t1 = allocation[i].clone();
            int[] t2 = need[i].clone();
            available = MatrixUtils.add1D(available, t1);

            allocation[i] = new int[allocation[i].length];
            need[i] = new int[need[i].length];

            if (isSafeState()) {
                processesExecutionOrder[processesExecutionOrder.length - 1] = i;
                return true;
            }
            System.out.println("Didn't Work :(");
            allocation[i] = t1;
            need[i] = t2;
            available = MatrixUtils.subtract1D(available, t1);
        }
        return false;
    }

    private void readMatrices(String matricesFileName) throws FileNotFoundException {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(matricesFileName);
        if (inputStream == null) {
            throw new FileNotFoundException(
                    String.format("Matrices file (%s) was not found in resources", matricesFileName));
        }
        Scanner scanner = new Scanner(inputStream);
        processCount = scanner.nextInt();
        resourceCount = scanner.nextInt();
        allocation = new int[processCount][resourceCount];
        need = new int[processCount][resourceCount];
        available = new int[resourceCount];
        int[][] maxInstancesPerProcess = new int[processCount][resourceCount];
        int[] totalResourceInstances = new int[resourceCount];
        int[] totalAllocatedInstances = new int[resourceCount];

        for (int i = 0; i < processCount; i++) {
            for (int j = 0; j < resourceCount; j++) {
                int allocated = scanner.nextInt();
                allocation[i][j] = allocated;
                totalAllocatedInstances[j] += allocated;
            }
        }

        for (int i = 0; i < processCount; i++) {
            for (int j = 0; j < resourceCount; j++) {
                maxInstancesPerProcess[i][j] = scanner.nextInt();
            }
        }

        for (int i = 0; i < resourceCount; i++) {
            totalResourceInstances[i] = scanner.nextInt();
        }

        available = MatrixUtils.subtract1D(totalResourceInstances, totalAllocatedInstances);

        for (int i = 0; i < processCount; i++) {
            for (int j = 0; j < resourceCount; j++) {
                need[i][j] = maxInstancesPerProcess[i][j] - allocation[i][j];
            }
        }
        scanner.close();
    }
}
