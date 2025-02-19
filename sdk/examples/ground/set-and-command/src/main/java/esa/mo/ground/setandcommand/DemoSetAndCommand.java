/* ----------------------------------------------------------------------------
 * Copyright (C) 2021      European Space Agency
 *                         European Space Operations Centre
 *                         Darmstadt
 *                         Germany
 * ----------------------------------------------------------------------------
 * System                : ESA NanoSat MO Framework
 * ----------------------------------------------------------------------------
 * Licensed under European Space Agency Public License (ESA-PL) Weak Copyleft – v2.4
 * You may not use this file except in compliance with the License.
 *
 * Except as expressly set forth in this License, the Software is provided to
 * You on an "as is" basis and without warranties of any kind, including without
 * limitation merchantability, fitness for a particular purpose, absence of
 * defects or errors, accuracy or non-infringement of intellectual property rights.
 * 
 * See the License for the specific language governing permissions and
 * limitations under the License. 
 * ----------------------------------------------------------------------------
 */
package esa.mo.ground.setandcommand;

import esa.mo.nmf.groundmoadapter.GroundMOAdapterImpl;
import org.ccsds.moims.mo.common.directory.structures.ProviderSummary;
import org.ccsds.moims.mo.common.directory.structures.ProviderSummaryList;
import org.ccsds.moims.mo.mal.MALException;
import org.ccsds.moims.mo.mal.MALInteractionException;
import org.ccsds.moims.mo.mal.structures.URI;

import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Set and Command demo application. This demo should be used with the Hello World (Simple) demo provider.
 * Supervisor's directory URI is required as a command-line argument.
 */
public class DemoSetAndCommand
{
  private static final String PROVIDER_HELLO_WORLD = "App: hello-world";
  private static final Logger LOGGER = Logger.getLogger(DemoSetAndCommand.class.getName());

  /**
   *
   * @param directoryURI - supervisor directory URI
   *                     - e.g. "maltcp://123.123.123.123:1024/nanosat-mo-supervisor-Directory"
   */
  public DemoSetAndCommand(String directoryURI)
  {
    try {
      GroundMOAdapterImpl gma = null;
      ProviderSummaryList providers = GroundMOAdapterImpl.retrieveProvidersFromDirectory(
              new URI(directoryURI));

      if (!providers.isEmpty()) {
        for (ProviderSummary provider : providers) {
          if(provider.getProviderName().toString().equals(PROVIDER_HELLO_WORLD))
          {
            gma = new GroundMOAdapterImpl(provider);
            break;
          }
        }
      } else {
        LOGGER.log(Level.SEVERE,
                "The returned list of providers is empty!");
      }

      if(gma != null) {
        // Set a parameter with a string value
        String parameterValue = "The parameter was set!";
        gma.setParameter("A_Parameter", parameterValue);

        // Send a command with a Double argument
        double value = 1.35565;
        Double[] values = new Double[1];
        values[0] = value;
        gma.launchAction("Go", values);
      }
      else {
        LOGGER.log(Level.SEVERE, "Failed to connect to the provider. No such provider found - " +
                PROVIDER_HELLO_WORLD);
      }
    } catch (MALException | MalformedURLException | MALInteractionException ex) {
      LOGGER.log(Level.SEVERE, null, ex);
    }
  }

  /**
   * Main command line entry point.
   *
   * @param args the command line arguments
   *         - directoryURI required
   * @throws java.lang.Exception If there is an error
   */
  public static void main(final String args[]) throws Exception
  {
    if (args.length != 1) {
      System.err.println("Please give supervisor directory URI as an argument!");
      System.err.println("e.g. maltcp://123.123.123.123:1024/nanosat-mo-supervisor-Directory");
      System.exit(1);
    }
    DemoSetAndCommand demo = new DemoSetAndCommand(args[0]);
    return;
  }
}
