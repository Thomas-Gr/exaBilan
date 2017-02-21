package com.exabilan.interfaces;

import com.exabilan.types.exalang.Bilan;
import com.exabilan.types.structure.Document;

public interface ContentGenerator {

    Document createDocument(Bilan bilan);

}
