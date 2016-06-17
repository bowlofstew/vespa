// Copyright 2016 Yahoo Inc. Licensed under the terms of the Apache 2.0 license. See LICENSE in the project root.
package com.yahoo.vespa.model.builder.xml.dom;

import com.yahoo.text.XML;
import com.yahoo.config.model.producer.AbstractConfigProducer;
import com.yahoo.vespa.model.search.Tuning;
import org.w3c.dom.Element;

/**
 * Builder for the tuning config for a search cluster.
 *
 * @author geirst
 */
public class DomSearchTuningBuilder extends VespaDomBuilder.DomConfigProducerBuilder<Tuning> {

    @Override
    protected Tuning doBuild(AbstractConfigProducer parent, Element spec) {
        Tuning tuning = new Tuning(parent);
        for (Element e : XML.getChildren(spec)) {
            if (equals("dispatch", e)) {
                handleDispatch(e, tuning);
            } else if (equals("searchnode", e)) {
                handleSearchNode(e, tuning);
            }
        }
        return tuning;
    }

    private static boolean equals(String name, Element e) {
        return name.equals(e.getNodeName());
    }

    private static String asString(Element e) {
        return e.getFirstChild().getNodeValue();
    }

    private static Long asLong(Element e) {
        return Long.parseLong(e.getFirstChild().getNodeValue());
    }

    private static Integer asInt(Element e) {
        return Integer.parseInt(e.getFirstChild().getNodeValue());
    }

    private static Double asDouble(Element e) {
        return Double.parseDouble(e.getFirstChild().getNodeValue());
    }

    private void handleDispatch(Element spec, Tuning t) {
        t.dispatch = new Tuning.Dispatch();
        for (Element e : XML.getChildren(spec)) {
            if (equals("max-hits-per-partition", e)) {
                t.dispatch.maxHitsPerPartition = asInt(e);
            }
        }
    }

    private void handleSearchNode(Element spec, Tuning t) {
        t.searchNode = new Tuning.SearchNode();
        for (Element e : XML.getChildren(spec)) {
            if (equals("requestthreads", e)) {
               handleRequestThreads(e, t.searchNode);
            } else if (equals("flushstrategy", e)) {
                handleFlushStrategy(e, t.searchNode);
            } else if (equals("resizing", e)) {
                handleResizing(e, t.searchNode);
            } else if (equals("index", e)) {
                handleIndex(e, t.searchNode);
            } else if (equals("attribute", e)) {
                handleAttribute(e, t.searchNode);
            } else if (equals("summary", e)) {
                handleSummary(e, t.searchNode);
            }
        }
    }

    private void handleRequestThreads(Element spec, Tuning.SearchNode sn) {
        sn.threads = new Tuning.SearchNode.RequestThreads();
        Tuning.SearchNode.RequestThreads rt = sn.threads;
        for (Element e : XML.getChildren(spec)) {
            if (equals("search", e)) {
                rt.numSearchThreads = asInt(e);
            } else if (equals("persearch", e)) {
                rt.numThreadsPerSearch = asInt(e);
            } else if (equals("summary", e)) {
                rt.numSummaryThreads = asInt(e);
            }
        }
    }

    private void handleFlushStrategy(Element spec, Tuning.SearchNode sn) {
        for (Element e : XML.getChildren(spec)) {
            if (equals("native", e)) {
                handleNativeStrategy(e, sn);
            }
        }
    }

    private void handleNativeStrategy(Element spec, Tuning.SearchNode sn) {
        sn.strategy = new Tuning.SearchNode.FlushStrategy();
        Tuning.SearchNode.FlushStrategy fs = sn.strategy;
        for (Element e : XML.getChildren(spec)) {
            if (equals("total", e)) {
                for (Element e2 : XML.getChildren(e)) {
                    if (equals("maxmemorygain", e2)) {
                        fs.totalMaxMemoryGain = asLong(e2);
                    } else if (equals("diskbloatfactor", e2)) {
                        fs.totalDiskBloatFactor = asDouble(e2);
                    }
                }
            } else if (equals("component", e)) {
                for (Element e2 : XML.getChildren(e)) {
                    if (equals("maxmemorygain", e2)) {
                        fs.componentMaxMemoryGain = asLong(e2);
                    } else if (equals("diskbloatfactor", e2)) {
                        fs.componentDiskBloatFactor = asDouble(e2);
                    } else if (equals("maxage", e2)) {
                        fs.componentMaxage = asDouble(e2);
                    }
                }
            } else if (equals("transactionlog", e)) {
                for (Element e2 : XML.getChildren(e)) {
                    if (equals("maxentries", e2)) {
                        fs.transactionLogMaxEntries = asLong(e2);
                    } else if (equals("maxsize", e2)) {
                        fs.transactionLogMaxSize = asLong(e2);
                    }
                }
            }
        }
    }

    private void handleResizing(Element spec, Tuning.SearchNode sn) {
        sn.resizing = new Tuning.SearchNode.Resizing();
        for (Element e : XML.getChildren(spec)) {
            if (equals("initialdocumentcount", e)) {
                sn.resizing.initialDocumentCount = asInt(e);
            }
        }
    }

    private void handleIndex(Element spec, Tuning.SearchNode sn) {
        sn.index = new Tuning.SearchNode.Index();
        for (Element e : XML.getChildren(spec)) {
            if (equals("io", e)) {
                sn.index.io = new Tuning.SearchNode.Index.Io();
                Tuning.SearchNode.Index.Io io = sn.index.io;
                for (Element e2 : XML.getChildren(e)) {
                    if (equals("write", e2)) {
                        io.write = Tuning.SearchNode.IoType.fromString(asString(e2));
                    } else if (equals("read", e2)) {
                        io.read = Tuning.SearchNode.IoType.fromString(asString(e2));
                    } else if (equals("search", e2)) {
                        io.search = Tuning.SearchNode.IoType.fromString(asString(e2));
                    }
                }
            }
        }
    }

    private void handleAttribute(Element spec, Tuning.SearchNode sn) {
        sn.attribute = new Tuning.SearchNode.Attribute();
        for (Element e : XML.getChildren(spec)) {
            if (equals("io", e)) {
                sn.attribute.io = new Tuning.SearchNode.Attribute.Io();
                for (Element e2 : XML.getChildren(e)) {
                    if (equals("write", e2)) {
                        sn.attribute.io.write = Tuning.SearchNode.IoType.fromString(asString(e2));
                    }
                }
            }
        }
    }

    private void handleSummary(Element spec, Tuning.SearchNode sn) {
        sn.summary = new Tuning.SearchNode.Summary();
        for (Element e : XML.getChildren(spec)) {
            if (equals("io", e)) {
                sn.summary.io = new Tuning.SearchNode.Summary.Io();
                for (Element e2 : XML.getChildren(e)) {
                    if (equals("write", e2)) {
                        sn.summary.io.write = Tuning.SearchNode.IoType.fromString(asString(e2));
                    } else if (equals("read", e2)) {
                        sn.summary.io.read = Tuning.SearchNode.IoType.fromString(asString(e2));
                    }
                }
            } else if (equals("store", e)) {
                handleSummaryStore(e, sn.summary);
            }
        }
    }

    private void handleSummaryStore(Element spec, Tuning.SearchNode.Summary s) {
        s.store = new Tuning.SearchNode.Summary.Store();
        for (Element e : XML.getChildren(spec)) {
            if (equals("cache", e)) {
                s.store.cache = new Tuning.SearchNode.Summary.Store.Component();
                handleSummaryStoreComponent(e, s.store.cache);
            } else if (equals("logstore", e)) {
                handleSummaryLogStore(e, s.store);
            }
        }
    }

    private void handleSummaryStoreComponent(Element spec, Tuning.SearchNode.Summary.Store.Component c) {
         for (Element e : XML.getChildren(spec)) {
            if (equals("maxsize", e)) {
                c.maxSize = asLong(e);
            } else if (equals("maxentries", e)) {
                c.maxEntries = asLong(e);
            } else if (equals("initialentries", e)) {
                c.initialEntries = asLong(e);
            } else if (equals("compression", e)) {
                c.compression = new Tuning.SearchNode.Summary.Store.Compression();
                handleSummaryStoreCompression(e, c.compression);
            }
        }
    }

    private void handleSummaryStoreCompression(Element spec, Tuning.SearchNode.Summary.Store.Compression c) {
        for (Element e : XML.getChildren(spec)) {
            if (equals("type", e)) {
                c.type = Tuning.SearchNode.Summary.Store.Compression.Type.fromString(asString(e));
            } else if (equals("level", e)) {
                c.level = asInt(e);
            }
        }
    }

    private void handleSummaryLogStore(Element spec, Tuning.SearchNode.Summary.Store s) {
        s.logStore = new Tuning.SearchNode.Summary.Store.LogStore();
        for (Element e : XML.getChildren(spec)) {
            if (equals("maxfilesize", e)) {
                s.logStore.maxFileSize = asLong(e);
            } else if (equals("maxdiskbloatfactor", e)) {
                s.logStore.maxDiskBloatFactor = asDouble(e);
            } else if (equals("minfilesizefactor", e)) {
                s.logStore.minFileSizeFactor = asDouble(e);
            } else if (equals("numthreads", e)) {
                s.logStore.numThreads = asInt(e);
            } else if (equals("chunk", e)) {
                s.logStore.chunk = new Tuning.SearchNode.Summary.Store.Component(true);
                handleSummaryStoreComponent(e, s.logStore.chunk);
            }
        }
    }
}
