/*
 * Copyright (c) 2004-2014 Universidade do Porto - Faculdade de Engenharia
 * Laboratório de Sistemas e Tecnologia Subaquática (LSTS)
 * All rights reserved.
 * Rua Dr. Roberto Frias s/n, sala I203, 4200-465 Porto, Portugal
 *
 * This file is part of Neptus, Command and Control Framework.
 *
 * Commercial Licence Usage
 * Licencees holding valid commercial Neptus licences may use this file
 * in accordance with the commercial licence agreement provided with the
 * Software or, alternatively, in accordance with the terms contained in a
 * written agreement between you and Universidade do Porto. For licensing
 * terms, conditions, and further information contact lsts@fe.up.pt.
 *
 * European Union Public Licence - EUPL v.1.1 Usage
 * Alternatively, this file may be used under the terms of the EUPL,
 * Version 1.1 only (the "Licence"), appearing in the file LICENSE.md
 * included in the packaging of this file. You may not use this work
 * except in compliance with the Licence. Unless required by applicable
 * law or agreed to in writing, software distributed under the Licence is
 * distributed on an "AS IS" basis, WITHOUT WARRANTIES OR CONDITIONS OF
 * ANY KIND, either express or implied. See the Licence for the specific
 * language governing permissions and limitations at
 * https://www.lsts.pt/neptus/licence.
 *
 * For more information please see <http://lsts.fe.up.pt/neptus>.
 *
 * Author: zp
 * Jul 1, 2013
 */
package iridium;

import java.util.Collection;
import java.util.Vector;

import pt.lsts.imc.IMCDefinition;
import pt.lsts.imc.IMCInputStream;
import pt.lsts.imc.IMCMessage;
import pt.lsts.imc.IMCOutputStream;
import pt.lsts.imc.RemoteSensorInfo;

/**
 * @author zp
 *
 */
public class TargetAssetPosition extends IridiumMessage {
   
    public int asset_imc_id;
    public double lat, lon;
    
    public TargetAssetPosition() {
        super(2007);
    }

    @Override
    public int serializeFields(IMCOutputStream out) throws Exception {
        out.writeUnsignedShort(asset_imc_id);
        out.writeInt((int)Math.round(lat * 1000000));
        out.writeInt((int)Math.round(lon * 1000000));
        return 10;
    }

    @Override
    public int deserializeFields(IMCInputStream in) throws Exception {
        asset_imc_id = in.readUnsignedShort();
        lat = in.readInt() / 1000000.0;
        lon = in.readInt() / 1000000.0;
        return 10;
    }

   

    public final int getAssetImcId() {
        return asset_imc_id;
    }

    public final void setAssetImcId(int asset_imc_id) {
        this.asset_imc_id = asset_imc_id;
    }

    @Override
    public Collection<IMCMessage> asImc() {
        Vector<IMCMessage> msgs = new Vector<>();

        RemoteSensorInfo sensorInfo = new RemoteSensorInfo();
        sensorInfo.setLat(Math.toRadians(lat));
        sensorInfo.setLon(Math.toRadians(lon));
        sensorInfo.setSensorClass("Target Position");
        sensorInfo.setAlt(0);
        sensorInfo.setId("TP_"+IMCDefinition.getInstance().getResolver().resolve(asset_imc_id));
        sensorInfo.setSrc(getSource());
        sensorInfo.setDst(getDestination());
        msgs.add(sensorInfo);        

        return msgs;
    }
}
