/*
 *
 *  * Copyright 2016 Skymind,Inc.
 *  *
 *  *    Licensed under the Apache License, Version 2.0 (the "License");
 *  *    you may not use this file except in compliance with the License.
 *  *    You may obtain a copy of the License at
 *  *
 *  *        http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  *    Unless required by applicable law or agreed to in writing, software
 *  *    distributed under the License is distributed on an "AS IS" BASIS,
 *  *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *    See the License for the specific language governing permissions and
 *  *    limitations under the License.
 *
 */

package org.deeplearning4j.scalnet.layers.convolutional

import org.deeplearning4j.nn.conf.layers.ConvolutionLayer
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.scalnet.layers.Layer
import org.deeplearning4j.scalnet.regularizers.{NoRegularizer, WeightRegularizer}
import org.nd4j.linalg.activations.{Activation, IActivation}


/**
  * 2D convolution for structured image-like inputs. Input should have
  * three dimensions: height (number of rows), width (number of columns),
  * and number of channels. Convolution is over height and width.
  *
  * @author David Kale
  */
class Convolution2D(
                     nFilter: Int,
                     kernelSize: List[Int],
                     nChannels: Int = 0,
                     stride: List[Int] = List(1, 1),
                     padding: List[Int] = List(0, 0),
                     nIn: Option[List[Int]] = None,
                     val weightInit: WeightInit = WeightInit.XAVIER_UNIFORM,
                     val activation: Activation = Activation.IDENTITY,
                     val regularizer: WeightRegularizer = NoRegularizer(),
                     val dropOut: Double = 0.0,
                     override val name: String = "")
  extends Convolution(kernelSize, stride, padding, nChannels, nIn, nFilter)
    with Layer {

  override def reshapeInput(nIn: List[Int]): Convolution2D = {
    new Convolution2D(nFilter, kernelSize, nChannels, stride, padding, Some(nIn),
      weightInit, activation, regularizer, dropOut, name)
  }

  override def compile: org.deeplearning4j.nn.conf.layers.Layer =
    new ConvolutionLayer.Builder(kernelSize.head, kernelSize.last)
      .nIn(inputShape.last)
      .nOut(outputShape.last)
      .stride(stride.head, stride.last)
      .padding(padding.head, padding.last)
      .weightInit(weightInit)
      .activation(activation)
      .l1(regularizer.l1)
      .l2(regularizer.l2)
      .dropOut(dropOut)
      .name(name)
      .build()
}

object Convolution2D {
  def apply(nFilter: Int,
            kernelSize: List[Int],
            nChannels: Int = 0,
            stride: List[Int] = List(1, 1),
            padding: List[Int] = List(0, 0),
            nIn: Option[List[Int]] = None,
            weightInit: WeightInit = WeightInit.XAVIER_UNIFORM,
            activation: Activation = Activation.IDENTITY,
            regularizer: WeightRegularizer = NoRegularizer(),
            dropOut: Double = 0.0): Convolution2D = {
    new Convolution2D(nFilter, kernelSize, nChannels, stride, padding, nIn,
      weightInit, activation, regularizer, dropOut)
  }
}


