# spark-classifier
Supervised classification of research articles on large-scale high-dimensional data with Apache Spark

This Java code implements a parallel Spark algorithm for classifying research articles in large-scale document collections. The algorithm was presented in:

L. Akritidis, P. Bozanis, A. Fevgas, "Supervised Papers Classification on Large-Scale High-Dimensional Data with Apache Spark", In Proceedings of the 4th IEEE International Conference on Big Data Intelligence and Computing (DataCom), pp. 987-994, 2018. 

In contrast to the existing approaches, this method takes into consideration not only the title words and the keywords, but also  the history of the authors, co-authorship information, and the areas of science published by each journal. It was compared against the multi-class classifiers of Spark MLlib, [Logistic Regression](https://spark.apache.org/docs/latest/ml-classification-regression.html#logistic-regression), [Decision Trees](https://spark.apache.org/docs/latest/ml-classification-regression.html#decision-tree-classifier) and [Random Forests](https://spark.apache.org/docs/latest/ml-classification-regression.html#random-forest-classifier) be employing the [Open Academic Graph dataset](https://www.openacademic.ai/oag/).

None of the adversary approaches was able to complete the task in the original dataset. Instead, a dimensionality reduction method such as [Sparse Random Projection](https://github.com/SashiDareddy/RandomProjection).

The experiments indicated that our approach achieved higher classification accuracy, accompanied by substantially better execution times. Moreover, it was the only method which handled efficiently the huge dimensionality of the dataset, without requiring an additional dimensionality reduction technique.

**Note: The researchers who found that code useful are kindly asked to cite the aforementioned paper into their work/s.**
