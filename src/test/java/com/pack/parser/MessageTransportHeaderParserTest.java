package com.pack.parser;

import com.pack.model.Email;
import org.junit.Assert;
import org.junit.Test;

import java.util.Map;
import java.util.Queue;

public class MessageTransportHeaderParserTest {


    @Test
    public void testTransportHeader(){
        Map<String, Map<String, Queue<Email>>> headers = MessageTransportHeaderParser.parseVer1(getSampleHeader());
        Map<String, Queue<Email>> toList = headers.get("TO");
        Map<String, Queue<Email>> ccList = headers.get("CC");

        Assert.assertEquals(2, toList.size());
        Assert.assertEquals(2, ccList.size());
    }

    private String getSampleHeader(){
        return "Received: from BLAPR20MB3763.namprd20.prod.outlook.com (2603:10b6:208:30f::20)\n" +
                " by CH0PR20MB3995.namprd20.prod.outlook.com with HTTPS; Fri, 9 Jul 2021\n" +
                " 13:02:07 +0000\n" +
                "Received: from BLAPR20MB3889.namprd20.prod.outlook.com (2603:10b6:208:320::14)\n" +
                " by BLAPR20MB3763.namprd20.prod.outlook.com (2603:10b6:208:30f::20) with\n" +
                " Microsoft SMTP Server (version=TLS1_2,\n" +
                " cipher=TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384) id 15.20.4308.22; Fri, 9 Jul\n" +
                " 2021 13:01:58 +0000\n" +
                "Authentication-Results: onetrust.com; dkim=none (message not signed)\n" +
                " header.d=none;onetrust.com; dmarc=none action=none header.from=onetrust.com;\n" +
                "Received: from BLAPR20MB3858.namprd20.prod.outlook.com (2603:10b6:208:323::8)\n" +
                " by BLAPR20MB3889.namprd20.prod.outlook.com (2603:10b6:208:320::14) with\n" +
                " Microsoft SMTP Server (version=TLS1_2,\n" +
                " cipher=TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384) id 15.20.4308.19; Fri, 9 Jul\n" +
                " 2021 13:01:53 +0000\n" +
                "Received: from BLAPR20MB3858.namprd20.prod.outlook.com\n" +
                " ([fe80::b84f:cfb9:dfd3:1c0b]) by BLAPR20MB3858.namprd20.prod.outlook.com\n" +
                " ([fe80::b84f:cfb9:dfd3:1c0b%4]) with mapi id 15.20.4308.023; Fri, 9 Jul 2021\n" +
                " 13:01:53 +0000\n" +
                "Content-Type: application/ms-tnef; name=\"winmail.dat\"\n" +
                "Content-Transfer-Encoding: binary\n" +
                "From: Alpha <alpha@onetrust.com>\n" +
                "To: Beeta <beeta@onetrust.com>, Gama <gama@onetrust.com>\n" +
                "CC: Abc <abc@onetrust.com>, Xyz <xyz@onetrust.com>\n" +
                "Subject: Sample Message\n" +
                "Thread-Topic: Mobile/CTV - Release Demo\n" +
                "Thread-Index: AdYvfwortFKr0uqQz0u2Etuz/xArwFFQoco6\n" +
                "Date: Fri, 9 Jul 2021 13:01:53 +0000\n" +
                "Message-ID:\n" +
                " <BLAPR20MB3858D98C8B1C34D7CD013988AC189@BLAPR20MB3858.namprd20.prod.outlook.com>\n" +
                "References:\n" +
                " <BN8PR20MB22435F09708B800057B198ACAC930@BN8PR20MB2243.namprd20.prod.outlook.com>\n" +
                "In-Reply-To:\n" +
                " <BN8PR20MB22435F09708B800057B198ACAC930@BN8PR20MB2243.namprd20.prod.outlook.com>\n" +
                "Accept-Language: en-US\n" +
                "Content-Language: en-US\n" +
                "X-MS-Exchange-Organization-ModifySensitivityLabel:\n" +
                " ;d5a3b3d1-43a1-4cb9-bcfb-f51bbaaf59e2\n" +
                "X-MS-Has-Attach:\n" +
                "X-MS-Exchange-Organization-SCL: -1\n" +
                "X-MS-TNEF-Correlator:\n" +
                " <BLAPR20MB3858D98C8B1C34D7CD013988AC189@BLAPR20MB3858.namprd20.prod.outlook.com>\n" +
                "msip_labels:\n" +
                " MSIP_Label_d5a3b3d1-43a1-4cb9-bcfb-f51bbaaf59e2_Enabled=True;MSIP_Label_d5a3b3d1-43a1-4cb9-bcfb-f51bbaaf59e2_SiteId=9d1d17d8-372b-4b23-a9fc-1e5d895c89a1;MSIP_Label_d5a3b3d1-43a1-4cb9-bcfb-f51bbaaf59e2_SetDate=2021-07-09T12:54:34.5250898Z;MSIP_Label_d5a3b3d1-43a1-4cb9-bcfb-f51bbaaf59e2_ContentBits=0;MSIP_Label_d5a3b3d1-43a1-4cb9-bcfb-f51bbaaf59e2_Method=Standard\n" +
                "MIME-Version: 1.0\n" +
                "X-MS-Exchange-Organization-MessageDirectionality: Originating\n" +
                "X-MS-Exchange-Organization-AuthSource: BLAPR20MB3858.namprd20.prod.outlook.com\n" +
                "X-MS-Exchange-Organization-AuthAs: Internal\n" +
                "X-MS-Exchange-Organization-AuthMechanism: 04\n" +
                "X-Microsoft-Antispam-Mailbox-Delivery:\n" +
                "\tucf:0;jmr:0;auth:0;dest:I;ENG:(750129)(520011016)(706158);\n" +
                "X-Microsoft-Antispam-Message-Info:\n" +
                "\tRp77O6zAECEakirLQDDxhMQYHPK5XnTgozodkkMf75UQaYkk4wdfCctgF6REyD0WfvCUwTwC0q8h0ZMTnZxkxDkuEYFwLbHeD+/3OH61qst/dfVgurZQ2tlXRD+tX2FLLhw8qZrC0azy5toB7pW2sW3lc/yizi2R+axBb/F+OpFWlP2bBsGzchvJgqhUqhLyzof/RNDkvk5ZlA7Kgj9asn7GdospLcY1svxDELgfJMETK/Rvq9bxeXVXhSqkaJT8CW1SWYm8GcJrhH26Qys9jarRYfi/2p4/4OzjHlkgvBTBQ8Eauzih4e7sGBUGYaA2u8O77etm19HVYHOU6/Rea+CZQozYeLy0J/cRyGw2nYJ1h4FGB7D7bIegRagU/ixpIhIdkU8Yqan84yk/hqdQ+Tmv33Vq0QG39oKEKUwvf8dZv8jIq5L6ai6KKBH8fVIHCso71G6rgLzBxSNADjnuIW6kVHmK1ZxQprDiFoYG54hDWfxUvfqC3bV0b0BEJXOZGryA7V0Fuk8aFYIW+bETTWz7iyCJuJIs8eobdcRZv6Eix2i/ZyU/w4gm6YxxPMSZfExLpqoxOTfiJPjTtcXA2Or2kVY+cV+rDPdcLxHZOy3o5SzXiuZF40BvSPQdg/FspG+zPlXRFtF3awmNC9KVw5ykJ7h2cZWtxCzYeWovLtD+1E1mQjeusoDY2Q8dOoO/h5Ca330CQwfOQNT7RDZzr8kHWJSsI+q+PPrmhd225hD0GAw4PIXyEIs8eYtGM7WaPJUMhOWCkrpPnIGS0alTyrpKY2Co4Bus+7HdHNdERHE=\n" +
                "X-MS-Exchange-Safelinks-Url-KeyVer: 1";
    }

}