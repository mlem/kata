package pony.independent

class PonyInteraction {

    String input

    def dropLargest(List list) {
        partySort(list)
        def removedPony = list.remove(0)
        list.each {
            it.relations -= removedPony
        }
        list
    }

    def partySort(List input) {
        input.sort { VertexNode a, VertexNode b ->
            b.relations.size().compareTo(a.relations.size())
        }
    }

    RawData toRawData(String input) {
        def data = input.readLines()*.split(' ').collect { it*.toInteger() }
        def records = data.subList(1, data.size())
        new RawData(numberOfPonies: data[0][0], records: records)
    }


    Collection<VertexNode> parseGraph(inputString) {
        RawData rawData = toRawData(inputString)
        if (rawData.numberOfPonies == 0) return []
        toRelationsGraph(rawData)
    }

    private Collection<VertexNode> toRelationsGraph(RawData rawData) {
        Collection<VertexNode> graph = initializeGraph(rawData.numberOfPonies)
        rawData.records.each { edge ->
            graph[edge[0]].relations << graph[edge[1]]
            graph[edge[1]].relations << graph[edge[0]]
        }
        graph
    }

    private Collection<VertexNode> initializeGraph(int numberOfPonies) {
        (0..numberOfPonies - 1).collect([]) {
            return new VertexNode(node: it)
        }
    }

    public int getNumberOfPoniesToInvite() {
        def graph = parseGraph(input)
        while(graph.any {!it.relations.empty}) {
            graph = new PonyInteraction().dropLargest(graph)
        }
        return graph.size()
    }

    class VertexNode {
        def node
        def relations = []

        Map toMap() {
            def map = [:]
            map.put(node, relations*.node)
            return map
        }

        String toString() {
            return "VertexNode [$node][${relations*.node.join(',')}]"
        }
    }

    class RawData {
        int numberOfPonies
        def records
    }
}
