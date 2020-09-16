/* NFCard is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 3 of the License, or
(at your option) any later version.

NFCard is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with Wget.  If not, see <http://www.gnu.org/licenses/>.

Additional permission under GNU GPL version 3 section 7 */

package project.bridgetek.com.applib.main.toos.Card;

import android.content.res.Resources;
import android.nfc.tech.NfcV;


final class VicinityCard {

    public static final int SW1_OK = 0x00;

    static String load(NfcV tech, Resources res) {
        String data = null;
        try {
            tech.connect();

            int pos, BLKSIZE, BLKCNT;
            byte cmd[], rsp[], ID[], RAW[], STA[], flag, DSFID;

            ID = tech.getTag().getId();
            if (ID == null || ID.length != 8)
                throw new Exception();

            /*--------------------------------------------------------------*/
            // get system information
            /*--------------------------------------------------------------*/
            cmd = new byte[10];
            cmd[0] = (byte) 0x22; // flag
            cmd[1] = (byte) 0x2B; // command
            System.arraycopy(ID, 0, cmd, 2, ID.length); // UID

            rsp = tech.transceive(cmd);
            if (rsp[0] != SW1_OK)
                throw new Exception();

            pos = 10;
            flag = rsp[1];

            DSFID = ((flag & 0x01) == 0x01) ? rsp[pos++] : 0;

            if ((flag & 0x02) == 0x02)
                pos++;

            if ((flag & 0x04) == 0x04) {
                BLKCNT = rsp[pos++] + 1;
                BLKSIZE = (rsp[pos++] & 0xF) + 1;
            } else {
                BLKCNT = BLKSIZE = 0;
            }

            /*--------------------------------------------------------------*/
            // read first 8 block
            /*--------------------------------------------------------------*/
            cmd = new byte[12];
            cmd[0] = (byte) 0x22; // flag
            cmd[1] = (byte) 0x23; // command
            System.arraycopy(ID, 0, cmd, 2, ID.length); // UID
            cmd[10] = (byte) 0x00; // index of first block to get
            cmd[11] = (byte) 0x07; // block count, one less! (see ISO15693-3)

            rsp = tech.transceive(cmd);
            if (rsp[0] != SW1_OK)
                throw new Exception();

            RAW = rsp;

            /*--------------------------------------------------------------*/
            // read last block
            /*--------------------------------------------------------------*/
            cmd[10] = (byte) (BLKCNT - 1); // index of first block to get
            cmd[11] = (byte) 0x00; // block count, one less! (see ISO15693-3)

            rsp = tech.transceive(cmd);
            if (rsp[0] != SW1_OK)
                throw new Exception();

            STA = rsp;

            data = Util.toHexString(rsp, 0, rsp.length);

            /*--------------------------------------------------------------*/
            // build result string
            /*--------------------------------------------------------------*/
            final String info = parseInfo(ID, res);
            data = CardManager.buildResult(info);

        } catch (Exception e) {
            data = null;
            // data = e.getMessage();
        }

        try {
            tech.close();
        } catch (Exception e) {
        }
        return data;
    }

    private static String parseInfo(byte[] id, Resources res) {
        final StringBuilder r = new StringBuilder();
        final String i = "标签：";
        r.append(Util.toHexStringR(id, 0, id.length));

        return r.toString();
    }

}
